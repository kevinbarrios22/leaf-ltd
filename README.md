# Leaf Ltd — Business Management API

Spring Boot 3.3.4 REST API for managing employees, attendance, delivery forms, delivery sheets, damaged products, and system users. Serves a frontend SPA (React/Vite) via CORS configuration.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Runtime | Java 21 (JDK 21+) |
| Framework | Spring Boot 3.3.4 |
| Database | PostgreSQL 16+ |
| ORM | Hibernate 6 + Spring Data JPA |
| Auth | JWT (jjwt 0.11.5) — 24h tokens + refresh |
| Validation | Jakarta Validation |
| Docs | Springdoc OpenAPI 2.6.0 (Swagger UI) |
| Build | Maven (wrapper included) |
| Dev | Lombok 1.18.34, DevTools, Actuator |

## Prerequisites

- **Java 21** — Lombok 1.18.34 is incompatible with JDK 22+
- **PostgreSQL 16+** — running on `localhost:5432`
- A database named **`leafltd`** (created manually beforehand)

### JDK 21 Setup (Windows)

If you have a newer JDK (22–26) installed, you need JDK 21 for this project. Download from [Adoptium](https://adoptium.net/temurin/releases/?version=21) and extract to a portable location:

```powershell
# Extract to a portable directory (no admin required)
# Example: C:\Users\keanb\java\jdk-21.0.6+7
$env:JAVA_HOME = "C:\path\to\jdk-21"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

## Quick Start

```bash
# 1. Set environment variables
export JAVA_HOME="/path/to/jdk-21"
export PATH="$JAVA_HOME/bin:$PATH"
export DB_PASSWORD="your_postgres_password"
export JWT_SECRET="your-secret-key-minimum-32-characters-long"

# 2. Create the database
createdb -U postgres leafltd

# 3. Run the application
./mvnw spring-boot:run
```

The server starts on **`http://localhost:8080`**.

### First Run

On first startup, the `DataInitializer` seeds:
- **Roles**: `BOSS`, `OFFICE`, `MANAGER`, `EMPLOYEE`
- **Admin user**: `admin` / `admin123` (BOSS role)

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `DB_PASSWORD` | Yes | — | PostgreSQL password |
| `JWT_SECRET` | Yes | — | HMAC-SHA256 key, **min 32 characters** |
| `CORS_ORIGINS` | No | `http://localhost:5173` | Comma-separated allowed origins |
| `PORT` | No | `8080` | Server port |

### Configuration files

```
src/main/resources/
├── application.yml          # Active config (gitignored — uses ${env} placeholders)
├── application.yml.example  # Template with placeholder values
└── .env.example             # Env var template
```

Copy the example file and fill in your values:

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

## Authentication

All endpoints except `/api/auth/**`, `/swagger-ui/**`, and `/v3/api-docs/**` require a JWT token.

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Response:
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "role": "BOSS"
  },
  "message": null,
  "errors": null,
  "timestamp": "2026-06-09T01:29:03.2125286"
}
```

### Using the token

Include it in the `Authorization` header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Refresh token

```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Authorization: Bearer <current_token>"
```

Returns a new token with a fresh 24h expiration. Useful for long sessions without requiring the user to re-login.

## API Standard

### Response Envelope

Every response follows the `ApiResponse<T>` wrapper:

```json
{
  "success": true|false,
  "data": { ... },
  "message": "optional message",
  "errors": { "field": "error message" },
  "timestamp": "2026-06-09T01:29:03.2125286"
}
```

### Error Handling

| Status | Scenario | Response |
|--------|----------|----------|
| 400 | Validation error | Field-level error map in `errors` |
| 401 | Bad credentials / expired token | `message` describes the issue |
| 403 | Insufficient role | Permission error message |
| 404 | Entity not found | Resource name in `message` |
| 500 | Unexpected error | Generic internal error |

### Pagination

List endpoints accept Spring Data `Pageable` parameters:

| Parameter | Default | Example |
|-----------|---------|---------|
| `page` | `0` | `?page=0` |
| `size` | `20` | `?size=10` |
| `sort` | varies | `?sort=id,desc` |

Response body (`data` field):
```json
{
  "content": [ ... ],
  "pageable": { ... },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "empty": false
}
```

## API Reference

### Auth — `/api/auth`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| POST | `/api/auth/login` | public | Authenticate and get JWT |
| POST | `/api/auth/register` | BOSS, OFFICE | Create a new system user |
| POST | `/api/auth/refresh` | public* | Renew JWT without re-login |

\* Requires a valid (not-yet-expired) token in the `Authorization` header.

### Users — `/api/users`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| GET | `/api/users` | BOSS, OFFICE | List all users (paginated, sorted by username) |
| PUT | `/api/users/{id}` | BOSS, OFFICE | Toggle user enabled/disabled |
| DELETE | `/api/users/{id}` | BOSS | Delete a user |

### Employees — `/api/employees`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| GET | `/api/employees` | BOSS, OFFICE | List active employees (paginated) |
| GET | `/api/employees/{id}` | BOSS, OFFICE, MANAGER | Get employee by ID |
| POST | `/api/employees` | BOSS, OFFICE, MANAGER | Create employee |
| PUT | `/api/employees/{id}` | BOSS, OFFICE, MANAGER | Update employee |
| DELETE | `/api/employees/{id}` | BOSS | Soft-delete (deactivate) employee |

### Attendance — `/api/attendance`

Daily status values: `"working"`, `"rest"`, `"disability"`, `"holiday"`, `""`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| POST | `/api/attendance/batch` | BOSS, OFFICE, MANAGER, EMPLOYEE | Batch save attendance for a date |
| GET | `/api/attendance/by-date` | BOSS, OFFICE, MANAGER, EMPLOYEE | Get records for a specific date (`?date=2026-06-09`) |
| GET | `/api/attendance/report` | BOSS, OFFICE | Monthly grid: employees × days (`?year=2026&month=6`) |

### Delivery Forms — `/api/delivery-forms`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| GET | `/api/delivery-forms` | BOSS, OFFICE, MANAGER | List forms (filter by `date`, `customer`, `driver`; paginated) |
| POST | `/api/delivery-forms` | BOSS, OFFICE, MANAGER, EMPLOYEE | Create form with customer rows |
| PUT | `/api/delivery-forms/{id}/rows` | BOSS, OFFICE, MANAGER | Update delivery status of rows |
| DELETE | `/api/delivery-forms/{id}` | BOSS, OFFICE | Delete form |

### Delivery Sheets — `/api/delivery`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| GET | `/api/delivery` | BOSS, OFFICE, MANAGER, EMPLOYEE | List sheets (filter by `date`, paginated) |
| GET | `/api/delivery/{id}` | BOSS, OFFICE, MANAGER | Get sheet by ID |
| POST | `/api/delivery` | BOSS, MANAGER, EMPLOYEE | Create sheet with invoices |
| PUT | `/api/delivery/{id}` | BOSS, MANAGER | Update sheet |
| DELETE | `/api/delivery/{id}` | BOSS | Delete sheet |

### Damaged Products — `/api/damaged`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| GET | `/api/damaged` | BOSS, OFFICE, MANAGER | List (filter by `date`, `itemNumber`; paginated) |
| GET | `/api/damaged/{id}` | BOSS, OFFICE, MANAGER | Get by ID |
| POST | `/api/damaged` | BOSS, OFFICE, MANAGER, EMPLOYEE | Register damaged product |
| PUT | `/api/damaged/{id}` | BOSS, OFFICE, MANAGER | Update record |
| DELETE | `/api/damaged/{id}` | BOSS | Delete record |

### Dashboard — `/api/dashboard`

| Method | Path | Roles | Description |
|--------|------|-------|-------------|
| GET | `/api/dashboard/stats` | BOSS, OFFICE, MANAGER, EMPLOYEE | Today's KPIs (active employees, attendance count, deliveries, damaged products) |

## Swagger UI

Browse and test all endpoints interactively:

- **Swagger UI**: [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs)

Swagger is configured with a global JWT Bearer auth scheme — click **Authorize** and paste your token to authenticate all requests.

## Architecture

```
HTTP Request
  │
  ├─ JwtAuthFilter (extract & validate Bearer token)
  │
  ├─ SecurityConfig (role-based access via @PreAuthorize)
  │
  ├─ Controller (validates input, delegates to service)
  │
  ├─ Service (business logic)
  │
  ├─ Repository (Spring Data JPA — queries the database)
  │
  └─ PostgreSQL (persistence)
       │
  ApiResponseAdvice (wraps all responses in ApiResponse<T>)
       │
  GlobalExceptionHandler (catches exceptions → ApiResponse.error)
       │
  JSON Response
```

### Project Layout

```
src/main/java/com/leaf/api_leaf/
├── ApiLeafApplication.java         # Entry point
├── config/                         # Beans, exception handler, CORS, Swagger, seed data
├── controller/                     # REST controllers (8 modules)
├── dto/
│   ├── request/                    # Inbound DTOs with validation
│   └── response/                   # Outbound DTOs (ApiResponse envelope)
├── enums/                          # RoleName, AttendanceStatus
├── model/                          # JPA entities (9 entities)
├── repository/                     # Spring Data JPA repositories
├── security/                       # JWT filter, util, UserDetailsService, SecurityConfig
└── service/                        # Business logic layer (8 services)
```

## Role Permissions Matrix

| Feature | BOSS | OFFICE | MANAGER | EMPLOYEE |
|---------|------|--------|---------|----------|
| Auth (register) | ✓ | ✓ | | |
| Users (CRUD) | ✓ | limited | | |
| Employees (CRUD) | ✓ | ✓ | partial | |
| Delivery Forms | ✓ | ✓ | partial | create only |
| Delivery Sheets | ✓ | partial | partial | get+create |
| Attendance | ✓ | ✓ | ✓ | ✓ |
| Damaged Products | ✓ | ✓ | partial | create only |
| Dashboard | ✓ | ✓ | ✓ | ✓ |

- **BOSS**: Full access including deletes
- **OFFICE**: Full access, limited deletes
- **MANAGER**: Operational access, no deletes
- **EMPLOYEE**: Basic self-service (attendance, report damage, deliveries)

## Development

### Re-compile on changes (DevTools active)

```bash
./mvnw compile
# DevTools auto-restarts the server when target/ classes change
```

### Run tests

```bash
./mvnw test
```

Requires a running PostgreSQL instance with the `DB_PASSWORD` env var set.

### Build

```bash
./mvnw package -DskipTests
java -jar target/api-leaf-0.0.1-SNAPSHOT.jar
```

### Troubleshooting

**Lombok compilation error (`java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN`)**

→ You're running JDK 22+ which is incompatible with Lombok 1.18.34. Install JDK 21.

**Port 8080 already in use**

→ Kill the existing process:
```powershell
netstat -ano | findstr :8080
taskkill /F /PID <PID>
```

**Database connection refused**

→ Ensure PostgreSQL is running and the `leafltd` database exists. Check `DB_PASSWORD` is set correctly.

## License

MIT
