# Copilot Instructions for CRUD-springboot

## Project Overview
- This is a Spring Boot CRUD application for managing articles, categories, orders, and order details.
- Main package: `com.techlab.crud`.
- Key layers: `controller`, `service`, `repository`, `model`.

## Architecture & Patterns
- **Controllers** (`controller/`): Handle HTTP requests. Example: `ArticulosController.java`, `PedidosController.java`.
- **Services** (`service/`): Business logic. Use interfaces (e.g., `ArticuloService`) and implementations (e.g., `ArticuloServiceImpl`).
- **Repositories** (`repository/`): Extend Spring Data JPA repositories for data access. Example: `ArticuloRepository.java`.
- **Models** (`model/`): JPA entities representing database tables.
- Follows standard Spring Boot layering and dependency injection.

## Developer Workflows
- **Build:** Use `mvnw clean install` (Windows: `mvnw.cmd clean install`).
- **Run:** Use `mvnw spring-boot:run` or run `ArticulosApplication.java` from your IDE.
- **Test:** Use `mvnw test` or run tests in `ArticulosApplicationTests.java`.
- **Configuration:** Main config in `src/main/resources/application.properties`.

## Conventions & Practices
- Service interfaces and implementations are split for testability and clarity.
- Repository interfaces extend `JpaRepository` or similar Spring Data interfaces.
- Controllers are annotated with `@RestController` or `@Controller`.
- Entities use JPA annotations (`@Entity`, `@Id`, etc.).
- Use constructor injection for services where possible.
- Keep business logic out of controllers.

## Integration & Dependencies
- Uses Spring Boot, Spring Data JPA, and Maven.
- External configuration via `application.properties`.
- No custom build or test scripts; standard Maven lifecycle applies.

## Examples
- To add a new entity: create a model, repository, service interface/impl, and controller.
- To add a new endpoint: update the relevant controller and service.

## Key Files & Directories
- `src/main/java/com/techlab/crud/model/` — JPA entities
- `src/main/java/com/techlab/crud/repository/` — Spring Data repositories
- `src/main/java/com/techlab/crud/service/` — Service interfaces and implementations
- `src/main/java/com/techlab/crud/controller/` — REST controllers
- `src/main/resources/application.properties` — Main configuration

---

For questions or unclear patterns, ask for clarification or check similar files in the relevant directory.