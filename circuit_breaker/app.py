from __future__ import annotations
import logging
import os
from contextlib import asynccontextmanager

import httpx
import pybreaker
from fastapi import FastAPI, Request, Response
from fastapi.responses import JSONResponse
from tenacity import retry, retry_if_exception, stop_after_attempt, wait_exponential_jitter

log = logging.getLogger("circuit_breaker-client")
logging.basicConfig(level=logging.INFO)

# ---- Parámetros ----
MAX_FAILS    = 10                     # Cantidad de fallas para pasar del estado CERRADO -> ABIERTO.
OPEN_INTERVAL_SECONDS  = 3            # Cuánto tiempo permanece ABIERTO antes de pasar a SEMI-ABIERTO.

TRANSIENT_STATUS: set[int] = {500, 502, 503, 504}   # Códigos de error transitorios y por culpa del servidor. 
RETRY_ATTEMPTS  = 3                   # Cantidad de re-intentos.

# URL del backend Java.
BACKEND_BASE_URL = os.getenv("BACKEND_BASE_URL", "")

def _is_transient(exc: BaseException) -> bool:
    """
        Define si es un error transitorio.
    """
    # Excepciones httpx que merecen un retry.
    if isinstance(exc, (httpx.ConnectTimeout, httpx.ReadTimeout, httpx.WriteError, httpx.ConnectError)):
        return True
    if isinstance(exc, httpx.HTTPStatusError):
        return exc.response.status_code in TRANSIENT_STATUS or exc.response.status_code == 429 # 429 Too Many Requests
    return False

# ---- Circuit Breaker con listeners para logging/métricas ----
class _CBListener(pybreaker.CircuitBreakerListener):
    def state_change(self, cb, old_state, new_state):
        if (old_state):
            log.warning("--- Circuit Breaker cambió de estado: %s -> %s", old_state.name, new_state.name)
        else:
            log.warning("--- Circuit Breaker tiene estado inicial: %s", new_state.name)
    def failure(self, cb, exc):
        log.debug("--- Circuit Breaker contó falla: %r", exc)
    def success(self, cb):
        log.debug("--- Circuit Breaker contó éxito")

circuit_breaker = pybreaker.CircuitBreaker(
    fail_max=MAX_FAILS,
    reset_timeout=OPEN_INTERVAL_SECONDS,
    listeners=[_CBListener()],
    exclude=list()
)

class ProtectedClient:
    def __init__(self, base_url: str, headers: dict[str, str] | None = None):
        self._client = httpx.Client(base_url=base_url, timeout=OPEN_INTERVAL_SECONDS, headers=headers or {})
    def close(self):
        self._client.close()

    def _classify(self, resp: httpx.Response) -> httpx.Response:
        # Trata a los errores 5xx/429 como transitorios (cuentan como fallas en el Circuit Breaker). 4xx => no re-intento; no es falla para el Circuit Breaker.
        if resp.status_code in TRANSIENT_STATUS or resp.status_code == 429:
            resp.raise_for_status()  # Se transforman en HTTPStatusError para Retry y Circuit Breaker.
        return resp

    @retry(
        retry=retry_if_exception(_is_transient),
        stop=stop_after_attempt(RETRY_ATTEMPTS),
        wait=wait_exponential_jitter(initial=0.25, max=2.0),
        reraise=True,
    )
    @circuit_breaker
    def _do(self, method: str, url: str, **kwargs) -> httpx.Response:
        resp = self._client.request(method, url, **kwargs)
        return self._classify(resp)

# ---- Servidor FastAPI (Circuit Breaker) ----
_client: ProtectedClient | None = None

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup.
    global _client
    if not BACKEND_BASE_URL:
        raise RuntimeError("BACKEND_BASE_URL no está definido")
    _client = ProtectedClient(base_url=BACKEND_BASE_URL)
    log.info("--- Circuit Breaker iniciado. BACKEND_BASE_URL=%s", BACKEND_BASE_URL)
    yield
    # Shutdown.
    if _client:
        _client.close()

app = FastAPI(title="Circuit Breaker", lifespan=lifespan)

def _strip_hop_by_hop(headers: dict[str, str]) -> dict[str, str]:
    drop = {"host","connection","keep-alive","te","trailers","transfer-encoding","upgrade"}
    return {k: v for k, v in headers.items() if k.lower() not in drop}

@app.api_route("/{full_path:path}", methods=["GET","POST"])
async def circuit_breaker_proxy(request: Request, full_path: str):
    assert _client is not None
    # Reconstruye URL relativa + querystring.
    target_path = "/" + full_path
    if request.url.query:
        target_path += "?" + request.url.query

    # Forward de body y headers (filtrando hop-by-hop).
    body = await request.body()
    headers = _strip_hop_by_hop(dict(request.headers))

    try:
        resp = _client._do(request.method, target_path, content=body, headers=headers)
    except pybreaker.CircuitBreakerError:
        return JSONResponse(status_code=503, content={"error": "backend no disponible (circuito ABIERTO)"})
    except httpx.HTTPError as e:
        return JSONResponse(status_code=502, content={"error": "error de gateway", "detail": str(e)})

    # Replica status y headers hacia el cliente.
    excluded = {"content-encoding","transfer-encoding","connection"}
    out_headers = {k: v for k, v in resp.headers.items() if k.lower() not in excluded}
    return Response(content=resp.content, status_code=resp.status_code, headers=out_headers)