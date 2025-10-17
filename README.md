### Fuel Order Service

A RESTful backend for managing fuel orders, customers, and order lifecycles. The service exposes CRUD and search endpoints, enforces role-based access, and persists data in PostgreSQL with schema migrations managed via Flyway. Authentication is stateless using JWT, making the API easy to scale horizontally.

The application is built on Spring Boot 3 and Java 17, using Spring Data JPA for persistence and validation for request payloads. It includes auditing, pagination, filtering with specifications, and consistent error handling. A Dockerfile is provided for containerized builds and runs.

## Tech Stack / Tools / Frameworks Used

- **Java 17**: LTS runtime for modern language features and performance.
- **Spring Boot 3.5.x**: Rapid bootstrap and opinionated configuration for REST services.
- **Spring Web**: To build the HTTP/REST controllers and request/response handling.
- **Spring Data JPA**: Simplifies database access with repositories and entity mapping.
- **Hibernate Validator (Validation)**: Bean validation for request DTOs and entities.
- **Spring Security**: Security foundation for authentication/authorization filters and config.
- **JWT (jjwt)**: Stateless authentication tokens between client and server.
- **PostgreSQL**: Relational database for durable storage of orders and users.
- **Flyway**: Versioned, repeatable database schema migrations.
- **Lombok**: Reduces boilerplate (getters, setters, builders, etc.).
- **Maven**: Build lifecycle, dependency management, and packaging.
- **Docker**: Containerized build and runtime for consistent deployments.

## Running the Application (Docker)

1) Build the image

```bash
docker build -t fuel-order-service .
```

2) Run the container

```bash
docker run \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://<host>:5432/fuel_order_db" \
  -e SPRING_DATASOURCE_USERNAME="postgres" \
  -e SPRING_DATASOURCE_PASSWORD="postgres" \
  --name fuel-order-service \
  fuel-order-service
```

The service listens on port `8081` (mapped from the container). On startup, Flyway applies migrations from `classpath:db/migration`.


### Environment Variables

- `SPRING_DATASOURCE_URL` (default: `jdbc:postgresql://localhost:5432/fuel_order_db`): JDBC URL for PostgreSQL.
- `SPRING_DATASOURCE_USERNAME` (default: `postgres`): Database username.
- `SPRING_DATASOURCE_PASSWORD` (default: `postgres`): Database password.
- `SPRING_JPA_HIBERNATE_DDL_AUTO` (default: `validate`): Hibernate schema management strategy.
- `JWT_SECRET` (default set in config): Secret used to sign JWTs; override in non-dev environments.
- `SERVER_PORT` is configured via `application.yml` as `8081` by default.


## Local Development (without Docker)

Prerequisites: Java 17, Maven, a running PostgreSQL instance.

1) Export environment variables (optional if you rely on defaults):

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/fuel_order_db"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="postgres"
export SPRING_JPA_HIBERNATE_DDL_AUTO="validate"
export JWT_SECRET="local-dev-secret"
```

2) Run the application:

```bash
mvn spring-boot:run
```

3) Or build and run the jar:

```bash
mvn -DskipTests package
java -jar target/fuel-order-0.0.1-SNAPSHOT.jar
```

4) Run tests:

```bash
mvn test
```


## API Documentation

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- A Postman collection is not bundled. You can derive requests from the controller endpoints and tests in `src/test/java`.

Download/import the sample Postman collection from `postman/FuelOrderService.postman_collection.json`.

Security note: Endpoints are protected with JWT via Spring Security. Obtain a token from the auth endpoints and send it as `Authorization: Bearer <token>`.


## Folder Structure

```
.
├── Dockerfile                 # Multi-stage Docker build (Maven -> JRE)
├── pom.xml                    # Maven project configuration
├── src
│   ├── main
│   │   ├── java/com/assignment/fuelorder
│   │   │   ├── web             # Controllers (Auth, FuelOrder)
│   │   │   ├── service         # Service interfaces and implementations
│   │   │   ├── repo            # Spring Data repositories and specifications
│   │   │   ├── entity          # JPA entities (e.g., FuelOrder, User)
│   │   │   ├── security        # JWT utilities, filters, and security config
│   │   │   ├── dto             # Request/response DTOs and constants
│   │   │   ├── mapper          # Mappers between entities and DTOs
│   │   │   └── config          # JPA auditing configuration
│   │   └── resources
│   │       ├── application.yml # App configuration (port 8081, DB, JWT)
│   │       └── db/migration    # Flyway SQL migrations (V1__, V2__, V3__)
│   └── test                    # Unit and integration tests
└── README.md
```


## Authentication Quick Start

1) Register or login via the auth endpoints to receive a JWT.
2) Include the token in subsequent requests: `Authorization: Bearer <token>`.
3) Access to order endpoints is restricted based on roles.


## Additional Notes

### Improvements / Next Steps

- Add health checks (`/actuator/health`), metrics, and tracing.
- Harden security: rotate `JWT_SECRET`, add token blacklist/refresh, tighten CORS.
- Introduce rate limiting and request logging/correlation IDs.
- CI/CD pipeline (build, test, SAST, container scan, deploy).
- Seed data profiles (dev vs prod) and feature toggles.


### Known Issues / Considerations

- The default `JWT_SECRET` in `application.yml` is for development only; override in all non-dev environments.
- Ensure database connectivity before startup; Flyway will attempt to run migrations.
- Sample data migration `V3__sample_data.sql` is included; review before enabling in production.


### Author & License
- **Author**: Chathuranga Wijerathna