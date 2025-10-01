# Universidad Católica del Uruguay

## Facultad de Ingeniería y Tecnologías

## Análisis y diseño de aplicaciones II

### Demo sobre SOLUCIONES DE ARQUITECTURA

GRUPO 5:

* Lucía Bonilla
* José Ignacio Lavecchia
* Leonardo Roy Arispe
* Gaspar Lamas
* Facundo Dutra

### Contexto

- Una API REST para gestionar proyectos, tareas y usuarios. Se centra en proyectos que contienen tareas, y en tareas asignadas a usuarios.
- El sistema utiliza una partición técnica y un enfoque ACID para garantizar la consistencia de los datos.
- Desarrollada en Java con Spring Boot, Spring Data JPA y PostgreSQL.
- Vease el informe de la TFU en Webasignatura para más información.

### Requisitos

* [maven](https://maven.apache.org/install.html)

### Pasos:

1. A nivel raíz del repositorio, ejecutar ``mvn clean package -DskipTests``

    * clean → Elimina el directorio /target antiguo (clases compiladas, JAR, etc.) para que pueda empezar de cero.
    * package → Compila el código y crea un artefacto ejecutable (un archivo .jar dentro de target/).
    * -DskipTests → Omite la ejecución de las pruebas unitarias de integración durante la compilación.

2. Levantar los contenedores Docker con ``docker-compose up -d --build``

3. Verificar que los contenedores están ejecutándose con ``docker ps``

    * projectmanager_app → Spring Boot API en puerto 8080
    * projectmanager_db → PostgreSQL en puerto 5432

### Test

Utilizar la colección de [Postman](UT3%20TFU.postman_collection.json) para testear los endpoints.