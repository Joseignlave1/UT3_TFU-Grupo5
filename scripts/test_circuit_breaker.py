import time
from datetime import datetime

import httpx

PROXY_BASE = "http://localhost:8000/api"
PATH_OK     = "/users"
# PATH_BAD  = "/does-not-exist"              # 404 example

REQS          = 60                            # Total de peticiones a enviar.
SLEEP_BETWEEN = 0.5                           # Segundos entre peticiones.

def ts() -> str:
    return datetime.now().strftime("%H:%M:%S")

def classify(resp: httpx.Response) -> str:
    # El Circuit Breaker manda los errores.
    try:
        data = resp.json()
    except Exception:
        data = None

    if resp.status_code == 200:
        return "OK"

    if resp.status_code == 503:
        return "CIRCUITO ABIERTO (error 503 - Service Unavailable)"

    if resp.status_code in (500, 502, 504):
        # Fallas transitorias que contribuyen a que el Circuit Breaker abra.
        return f"TRANSITORIO {resp.status_code} ({data or 'no-json'})"

    # Los errores de tipo 4xx no contribuyen a que el Circuit Breaker abra.
    if 400 <= resp.status_code < 500:
        return f"CLIENT {resp.status_code} (no cuenta como falla para el Circuit Breaker)"

    return f"OTHER {resp.status_code}"

def main():
    url = PROXY_BASE + PATH_OK
    print(f"[{ts()}] Objetivo: {url}")
    print("Mientras se ejecuta, apagar y prender la el contenedor de la app:")
    print("     docker compose stop app   # fuerza fallas")
    print("     docker compose start app  # restaura\n")

    with httpx.Client(timeout=2.5) as client:
        for i in range(1, REQS + 1):
            t0 = time.time()
            try:
                resp = client.get(url)
                label = classify(resp)
            except httpx.ConnectError as e:
                # Esta es una falla transitoria (503 Service Unavailable).
                label = f"CONNECT-ERROR ({e.__class__.__name__})"
                resp = None
            except httpx.ReadTimeout:
                label = "TIMEOUT (transitoria)"
                resp = None

            dt = (time.time() - t0) * 1000
            code = resp.status_code if resp is not None else "—"
            print(f"[{ts()}] #{i:02d} -> {code} | {label} | {dt:.0f} ms")

            time.sleep(SLEEP_BETWEEN)

if __name__ == "__main__":
    main()
