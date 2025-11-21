

## Project Overview
- Esta es una aplicacion CRUD de articulos.
- La aplicacion es un refactorizado del CRUD en consola, para seguir el modelo MVC.
- Paquete principal: `com.techlab.crud`.
- Etiquetas: `controller`, `service`, `repository`, `model`.

## Arquitectura y Patrones

- Controllers (controller/): Manejan las solicitudes HTTP. Ejemplos: ArticulosController.java, PedidosController.java.
- Services (service/): Contienen la lógica de negocio. Usan interfaces (por ej. ArticuloService) y sus implementaciones (ArticuloServiceImpl).
- Repositories (repository/): Extienden los repositorios de Spring Data JPA para acceder a la base de datos. Ejemplo: ArticuloRepository.java.
- Models (model/): Entidades JPA que representan las tablas de la base de datos.
- Sigue la estructura estándar de capas de Spring Boot y el uso de inyección de dependencias.

## Flujo de Trabajo del Desarrollador

- Build: Usar mvnw clean install (Windows: mvnw.cmd clean install).
- Ejecutar: Usar mvnw spring-boot:run o ejecutar ArticulosApplication.java desde el IDE.
- Test: Usar mvnw test o ejecutar los tests en ArticulosApplicationTests.java.
- Configuración: Archivo principal en src/main/resources/application.properties.

## Convenciones y Buenas Prácticas

- Las interfaces de servicios y sus implementaciones están separadas para mejorar la claridad y la capacidad de testeo.
- Los repositorios extienden JpaRepository u otras interfaces de Spring Data.
- Los controladores usan @RestController o @Controller.
- Las entidades usan anotaciones JPA (@Entity, @Id, etc.).
- Se recomienda usar inyección de dependencias por constructor.
- Mantener la lógica de negocio fuera de los controladores.

## Integración y Dependencias

- Usa Spring Boot, Spring Data JPA y Maven.
- Configuración externa mediante application.properties.
- No hay scripts personalizados; se usa el ciclo de vida estándar de Maven.

## Ejemplos

- Para agregar una nueva entidad: crear el modelo, el repositorio, la interfaz e implementación del servicio y el controlador.
- Para agregar un nuevo endpoint: actualizar el controlador y el servicio correspondiente.

## Archivos y Directorios Clave

- src/main/java/com/techlab/crud/model/ — Entidades JPA
- src/main/java/com/techlab/crud/repository/ — Repositorios Spring Data
- src/main/java/com/techlab/crud/service/ — Interfaces e implementaciones de servicios
- src/main/java/com/techlab/crud/controller/ — Controladores REST
- src/main/resources/application.properties — Configuración principal

Si querés, también puedo traducir comentarios dentro del código, revisar la estructura o mejorar tu documentación.