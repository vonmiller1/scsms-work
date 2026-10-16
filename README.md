# Smart Cleanliness and Sanitation Management System (SCSMS)

Spring Boot REST API for reporting sanitation complaints, attaching location and photos, and tracking resolution status for citizens and BMC (municipal) operators.

## Overview

SCSMS backs a citizen-facing reporting workflow: users register, sign in with server-side sessions, submit complaints with coordinates and optional images, and read or update complaint status. BMC accounts use a parallel auth flow. Data is stored in MySQL via Spring Data JPA. This repository contains the Java backend only; the React frontend is hosted separately (for example on Vercel).

## Features

- User registration and login with HTTP session cookies (`/api/auth/**`).
- BMC registration and login with separate session attribute (`/api/bmc/**`).
- Multipart complaint submission with description, area, lat/long, status, and optional file or URL.
- List, fetch-by-id, and status update endpoints for complaints.
- Static serving of locally uploaded images under `/uploads/**`.
- Health check at `/health` (unauthenticated).
- Swagger/Springfox UI for API exploration (when enabled).
- Docker Compose recipe for local MySQL + packaged JAR.

## Tech stack

| Layer | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 3.4.3 (Web, Data JPA, Security) |
| Database | MySQL 8 (Hibernate `ddl-auto=update`) |
| Build | Maven (wrapper included) |
| API docs | Springfox 3.0.0 |

## Prerequisites

- JDK 21
- MySQL 8 (local install or via `docker-compose.yml`)
- Optional: Docker Desktop for Compose-based setup

## Getting started

1. Clone the repository:

   ```powershell
   git clone https://github.com/vonmiller1/scsms-work.git
   cd scsms-work
   ```

2. Create a MySQL database (default name: `reportissuedb`).

3. Set credentials via environment variables (names only—do not commit secrets):

   | Variable | Purpose |
   | --- | --- |
   | `SPRING_DATASOURCE_URL` | JDBC URL (overrides host/db in `application.properties`) |
   | `SPRING_DATASOURCE_USERNAME` or `MYSQL_USERNAME` | DB user |
   | `SPRING_DATASOURCE_PASSWORD` or `MYSQL_PASSWORD` | DB password |
   | `MYSQL_HOSTNAME`, `MYSQL_PORT`, `MYSQL_DB_NAME` | Host, port, database when not using full JDBC URL |
   | `PORT` | HTTP port (default `8081`) |
   | `ADDRESS` | Bind address (default `0.0.0.0`) |

## Running locally

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/reportissuedb"
$env:SPRING_DATASOURCE_USERNAME = "your-local-user"
$env:SPRING_DATASOURCE_PASSWORD = "your-local-password"
.\mvnw.cmd spring-boot:run
```

The API listens on `http://localhost:8081` by default.

Build a runnable JAR:

```powershell
.\mvnw.cmd package
java -jar target\ROOT.jar
```

With Docker Compose (build the JAR first, then review credentials in `docker-compose.yml`):

```powershell
.\mvnw.cmd package
docker compose up
```

## Testing

Tests live under `src/main/java/com/springboot/application/ScsmsApplicationTests.java` in this project layout. Run them when you choose to validate changes:

```powershell
.\mvnw.cmd test
```

No automated tests were executed as part of repository maintenance passes unless explicitly noted elsewhere.

## Project structure

```
scsms-work/
├── pom.xml
├── docker-compose.yml
├── Procfile
├── src/main/java/com/springboot/
│   ├── application/     # Spring Boot entry point
│   ├── config/            # Security, CORS, Swagger, web MVC
│   ├── controller/        # REST endpoints
│   ├── model/             # JPA entities
│   ├── repository/        # Spring Data interfaces
│   └── service/           # Business logic
└── src/main/resources/
    └── application.properties
```

Uploaded complaint photos are written to `uploads/` at the application working directory (git-ignored).

## Configuration

Key settings in `src/main/resources/application.properties`:

- Session timeout: 30 minutes, cookie-based tracking.
- CORS: `http://localhost:3000` and `https://scsms.vercel.app` (see `WebConfig` and controller `@CrossOrigin`).
- HikariCP pool sizing and optional Hibernate SQL logging via `HIBERNATE_SHOW_SQL` / `HIBERNATE_FORMAT_SQL`.
- Multipart upload size limits (see properties file).

Align Spring Security `requestMatchers` with the routes you expose publicly (health, auth, Swagger).

## Deployment

- **Heroku-style**: `Procfile` runs `target/ROOT.jar` with `PORT` from the platform.
- **JAR**: `mvnw package` produces `target/ROOT.jar` (`finalName` in `pom.xml`).
- **Docker Compose**: MySQL + OpenJDK 21 container; set `PORT` and datasource env vars to match your network layout.

Use durable storage for `uploads/` in production or store images in object storage via `photoUrl`.

## API overview

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/auth/signup` | Register a user |
| `POST` | `/api/auth/login` | User login (session) |
| `GET` | `/api/auth/session-user` | Current user session |
| `POST` | `/api/auth/logout` | End user session |
| `POST` | `/api/bmc/signup` | Register BMC account |
| `POST` | `/api/bmc/login` | BMC login (session) |
| `GET` | `/api/bmc/session-bmc` | Current BMC session |
| `POST` | `/api/complaints` | Submit complaint (`multipart/form-data`) |
| `GET` | `/api/complaints` | List complaints (authenticated session) |
| `GET` | `/api/complaints/{id}` | Get one complaint |
| `PUT` | `/api/complaints/{id}` | Update status |
| `GET` | `/health` | Liveness check |

## Contributing

1. Fork the repository and create a feature branch from `main`.
2. Keep commits focused; one logical change per commit when possible.
3. Open a pull request with a short description of behavior and any security implications.
4. Do not commit credentials, `.env` files, or production uploads.

## License

No `LICENSE` file is present in this repository. Contact the repository owner before redistributing or using this software in production.
