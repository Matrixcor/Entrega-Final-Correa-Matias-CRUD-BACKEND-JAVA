## Project Overview API (Spring Boot & JPA)

- Esta es una aplicacion CRUD de Articulos, Pedidos, Usuarios.
- La aplicacion es un refactorizado del CRUD en consola.
- API RESTful construida con Spring Boot 3 para gestionar el flujo de Pedidos, Artículos y Usuarios, utilizando una arquitectura de microservicios con múltiples Bases de Datos (Multiple Data Sources) y gestión de transacciones distribuida.

# Tecnologías

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.x
* **Persistencia:** Spring Data JPA / Hibernate
* **Bases de Datos:** MySQL
* **Seguridad:** Spring Security
* **Transacciones:** `@Transactional` con Múltiples Transaction Managers
* **Testing:** JUnit 5
* **Herramienta de Construcción:** Maven

## Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

1.  **Java JDK 21** o superior.
2.  **Maven** (para la gestión de dependencias).
3.  **MySQL Server** (versión 8.0 o superior).

## Configuración de Base de Datos

Esta aplicación utiliza múltiples fuentes de datos. Debes crear y configurar las siguientes bases de datos en tu servidor MySQL y actualizar el archivo `application.properties` (o `application.yml`):

| Dominio       | Nombre de BD   | Propiedad a configurar           |
| :---          | :---           | :---                             |
| **Artículos** | `articulos_db` | `spring.datasource.articulos.url`|
| **Pedidos**   | `pedidos_db`   | `spring.datasource.pedidos.url`  |
| **Usuarios**  | `usuarios_db`  | `spring.datasource.usuarios.url` |


## Arquitectura y Patrones

**Diseño de Arquitectura**
- El proyecto está diseñado bajo una arquitectura de microservicios con dominios acoplados y separación de datos:
- Múltiples Fuentes de Datos (Multiple Data Sources): Los dominios principales (Pedidos, Artículos, Usuarios) utilizan bases de datos separadas para aislamiento de datos.
- Transacciones: La consistencia en operaciones complejas (ej. restar stock al crear un pedido) se asegura mediante Transaction Managers dedicados a cada base de datos.

**Patrón de Capas**
- Se sigue el patrón N-Tier (Capas) con responsabilidades claras:
- Controller: Maneja peticiones HTTP y validaciones de DTOs.
- Service: Contiene la Lógica de Negocio (ej. Reglas de transición de estado) y gestiona la transacción (@Transactional).
- Repository: Abstrae la interacción con la Base de Datos (JPA).

## Flujo de Trabajo del Desarrollador

- El proceso de desarrollo se centró en la implementación de la funcionalidad de los endpoint y demas secciones, siguiendo un ciclo de depurado constante:
**Definición y Diseño del Endpoint**
- Identificación del Requisito: Establecer la necesidad de actualizar el estado de un pedido específico.

- Decisión Arquitectónica: Se migró la forma de recibir el nuevo estado de un parámetro de consulta (@RequestParam) a un cuerpo de solicitud JSON (@RequestBody EstadoPedidoDTO), adoptando una mejor práctica REST para operaciones PATCH.

**Implementación de la Lógica de Negocio**
- Refuerzo de la Capa de Servicio: Se aseguró que toda la lógica de validación crítica residiera en el método pedidoService.updateEstado().

- Aplicación de Reglas de Negocio: Se implementó el método esTransicionValida para obligar a que el cambio de estado (ej. de ENVIADO a COMPLETADO) fuera lógico y permitido, garantizando la integridad de los datos.

**Diagnóstico y Corrección de Infraestructura**
- Debugging de Seguridad: Ante el error 403 Forbidden, se diagnosticó y corrigió la configuración de Spring Security (eliminando configuraciones redundantes de CSRF y asegurando el permitAll() en la ruta).

- Validación de Mapeo: Se ajustaron las anotaciones del controlador (@RequestBody, @PathVariable) y la configuración de Postman para asegurar que el mapping de la URL y el cuerpo JSON se resolvieran correctamente.

**Verificación Iterativa**
- Pruebas con Cliente REST: Se verificó la funcionalidad en cada paso utilizando Postman, probando la URL, el método (PATCH), y el cuerpo JSON hasta confirmar el código de respuesta 200 OK y la persistencia correcta del estado.

- Este proceso garantizó que tanto la funcionalidad como la robustez del endpoint fueran verificadas en tiempo real.

## Seguridad y Autenticación

- El acceso a las rutas `/api/pedidos/**`, `/api/articulos/**`, y `/api/usuarios/**` está configurado para **permitir el acceso libre** (`permitAll()`) para facilitar las pruebas.

*Nota: Para cualquier otra ruta no especificada, se requiere autenticación (Basic Auth).*

## Ejecución de la Aplicación

1.  **Clonar el repositorio:**
    
    git clone [https://github.com/Matrixcor/Entrega-Final-Correa-Matias-CRUD-BACKEND-JAVA.git]
    cd [nombre-del-repo]
    
2.  **Construir el proyecto con Maven:**
    
    mvn clean install
    
3.  **Ejecutar la aplicación Spring Boot:**

    mvn spring-boot:run

La aplicación estará disponible en `http://localhost:8080`.

## Endpoints de la API

La URL base para todos los endpoints es `http://localhost:8080/api/`

### Pedidos

| Método | URL                     | Descripción | Body (JSON) | Respuesta                                   |
| :---   | :---                    | :---        | :---        | :---                                        |
| `GET`  | `/pedidos`              | Obtiene la lista completa de pedidos. | - | `200 OK` (Lista de Pedidos) |
| `GET`  | `/pedidos/{pid}`        | Obtiene un pedido por su ID.          | - | `200 OK` (Detalle del Pedido) |
| `PATCH`| `/pedidos/{pid}/estado` | **Actualiza parcialmente el estado** de un pedido. | `{"estado": "ENVIADO"}` | `200 OK` (Pedido actualizado) |
| `POST` | `/pedidos`              | Crea un nuevo pedido. | (Pendiente de implementar) | `201 Created` |

#  Reglas de Transición de Estado

- La API valida las transiciones de estado (e.g., no se puede pasar de `COMPLETADO` a `PENDIENTE`). Estados válidos: **PENDIENTE, ENVIADO, COMPLETADO, CANCELADO**.

### Artículos

| Método  | URL                | Descripción | Body (JSON) | Respuesta |
| :---    | :---               | :--- | :--- | :--- |
| `GET`   | `/articulos`       | Obtiene la lista completa de artículos. | - | `200 OK` |
| `GET`   | `/articulos/{aid}` | Obtiene un artículo por su ID. | - | `200 OK` |
| `POST`  | `/articulos`       | Crea un nuevo artículo. | `{...}` (ArtículoRequestDTO) | `201 Created` |
| `PUT`   | `/articulos/{aid}` | Actualiza completamente un artículo. | `{...}` (ArtículoRequestDTO) | `200 OK` |
| `DELETE`| `/articulos/{aid}` | Elimina un artículo por su ID. | - | `204 No Content` |

### Categorías

| Método | URL          | Descripción | Body (JSON) | Respuesta |
| :---   | :---         | :---        | :--- | :--- |
| `GET`  | `/categoria` | Obtiene la lista completa de categorías. | - | `200 OK` |
| `POST` | `/categoria` | Crea una nueva categoría. | `{...}` (CategoríaRequestDTO) | `201 Created` |

### Usuarios

| Método | URL               | Descripción | Body (JSON) | Respuesta |
| :---   | :---              | :--- | :--- | :--- |
| `GET`  | `/usuarios`       | Obtiene la lista completa de usuarios. | - | `200 OK` |
| `GET`  | `/usuarios/{uid}` | Obtiene un usuario por su ID. | - | `200 OK` |
| `POST` | `/usuarios`       | Registra un nuevo usuario. | `{...}` (UsuarioRequestDTO) | `201 Created` |

## Archivos y Directorios Clave

- src/main/java/com/techlab/crud/model/ — Entidades JPA
- src/main/java/com/techlab/crud/config/ - Configuraciones base de datos y seguridad.
- src/main/java/com/techlab/crud/dto/ - Objetos de Transferencia de Datos para enviar y recibir datos a través de los endpoints.
- src/main/java/com/techlab/crud/exception/ - manejo de excepciones.
- src/main/java/com/techlab/crud/mapper/ - Mapeo de Entidades a DTOs y viceversa. 
- src/main/java/com/techlab/crud/repository/ — Repositorios Spring Data
- src/main/java/com/techlab/crud/service/ — Interfaces e implementaciones de servicios
- src/main/java/com/techlab/crud/controller/ — Controladores REST
- src/main/resources/application.properties — Configuración principal
