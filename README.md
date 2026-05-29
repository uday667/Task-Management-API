# Task Management API

A simple RESTful task management backend built with **Java 17**, **Spring Boot 3**, and **H2** (in‑memory). It follows basic Domain‑Driven Design principles and includes a full suite of unit and integration tests with **JUnit 5** and **Mockito** for 100 % code coverage.

## Features
- CRUD operations for tasks (`/tasks` endpoint).
- Validation: `title` and `dueDate` are required; `dueDate` must be a valid ISO‑8601 date.
- In‑memory H2 database (no external DB required).
- Clean layered architecture (controller → service → repository).
- Comprehensive tests (unit + integration).

## Prerequisites
- JDK 17 or higher
- Maven 3.8+

## Building the project
```bash
mvn clean install
```
This compiles the code, runs all tests, and packages the application.

## Running the application
```bash
mvn spring-boot:run
```
The API will be available at `http://localhost:8080`.

### H2 Console
The H2 console is enabled at `http://localhost:8080/h2-console`.
- JDBC URL: `jdbc:h2:mem:tasksdb`
- Username: `sa`
- No password

## API Endpoints
| Method | Path | Description |
|--------|------|-------------|
| **POST** | `/tasks` | Create a new task. Body JSON: `{ "title": "...", "description": "...", "status": "PENDING|IN_PROGRESS|DONE", "dueDate": "2026-06-01" }` |
| **GET** | `/tasks/{id}` | Retrieve a task by its ID. |
| **PUT** | `/tasks/{id}` | Update a task (partial fields allowed). |
| **DELETE** | `/tasks/{id}` | Delete a task (204 No Content on success). |
| **GET** | `/tasks` | List all tasks sorted by `dueDate`. |

## Testing
Run the test suite with:
```bash
mvn test
```
All tests should pass, providing full coverage.

## License
MIT – feel free to use and modify.
