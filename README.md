# Service Watch Microservice

A Spring Boot microservice for monitoring and tracking the health status of external service endpoints.

## Technologies

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Data JPA** - Database operations
- **Spring WebFlux** - Reactive HTTP client
- **PostgreSQL** - Database
- **Docker & Docker Compose** - Containerization
- **Maven** - Build tool
- **Lombok** - Code generation
- **OpenAPI/Swagger** - API documentation
- **JaCoCo** - Code coverage
- **Testcontainers** - Integration testing
- **SonarCloud** - Code quality analysis
- **GitHub Actions** - CI/CD pipeline

## Features

- Monitor external service endpoints
- Store service status logs
- Scheduled health checks
- RESTful API for managing endpoints
- Comprehensive test coverage
- Docker containerization

## Getting Started

### Prerequisites
- Java 17
- Docker & Docker Compose
- Maven

### Running with Docker
```bash
docker-compose up -d
```

### Running locally
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## API Documentation

Once running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Testing

Run tests with coverage:
```bash
mvn clean verify
```

## Project Structure

- **Controllers** - REST API endpoints
- **Services** - Business logic layer
- **Repositories** - Data access layer
- **Models** - JPA entities
- **DTOs** - Data transfer objects
- **Schedulers** - Background tasks for health checks
- **Facades** - Service orchestration layer

---

*This project is part of my portfolio demonstrating microservice architecture, Spring Boot, and modern Java development practices.*
