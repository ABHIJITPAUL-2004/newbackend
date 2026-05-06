# GriHom 2.0 — Backend (Spring Boot)

A complete production-quality Spring Boot 3 REST API backend for the GriHom home improvement advisory platform.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Spring Boot 3.2.5 | Core framework |
| Spring Web MVC | REST Controllers, `@RestController`, `@RequestBody`, `ResponseEntity` |
| Spring Data JPA | ORM, Repository layer, JPQL + Derived queries |
| Spring Security | JWT-based authentication, Role-based authorization |
| Spring Mail | Email sending (welcome, report saved) |
| Spring Actuator | `/actuator/health` endpoint |
| MySQL 8 | Production database |
| H2 | In-memory DB for tests |
| Lombok | `@Data`, `@Builder`, `@Slf4j`, `@RequiredArgsConstructor` |
| ModelMapper | Entity ↔ DTO mapping |
| JJWT 0.12.3 | JWT generation and validation |
| SLF4J + Logback | Structured logging with file rotation |
| JUnit 5 + Mockito | Unit testing |
| BCrypt | Password hashing |
| Jakarta Validation | `@Valid`, `@NotBlank`, `@Email`, `@Size` |
| Apache Commons | `StringUtils`, `IOUtils` |

---

## Project Structure

```
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/grihom/backend/
    │   │   ├── BackendApplication.java
    │   │   ├── config/
    │   │   │   ├── DataInitializer.java      ← Seeds admin + 6 improvements on startup
    │   │   │   ├── JacksonConfig.java        ← LocalDateTime serialization
    │   │   │   ├── ModelMapperConfig.java    ← Bean for DTO mapping
    │   │   │   ├── SecurityConfig.java       ← JWT + CORS + Role authorization
    │   │   │   └── WebConfig.java            ← Static file serving
    │   │   ├── controller/
    │   │   │   ├── HomeController.java       ← GET /api/test
    │   │   │   ├── AuthController.java       ← POST /api/auth/register, /login
    │   │   │   ├── ImprovementController.java← CRUD + history
    │   │   │   ├── ReportController.java     ← CRUD reports
    │   │   │   ├── PlannedController.java    ← Planned improvements
    │   │   │   ├── AdminController.java      ← User management
    │   │   │   ├── ReviewController.java     ← Reviews
    │   │   │   └── FileController.java       ← File upload/serve
    │   │   ├── dto/                          ← Request & Response DTOs
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java ← @ControllerAdvice
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   ├── UnauthorizedException.java
    │   │   │   └── DuplicateResourceException.java
    │   │   ├── model/
    │   │   │   ├── User.java                 ← @Entity with OneToMany
    │   │   │   ├── Improvement.java          ← @Entity with ManyToOne
    │   │   │   ├── Report.java
    │   │   │   ├── PlannedImprovement.java
    │   │   │   ├── ImprovementHistory.java
    │   │   │   ├── Review.java
    │   │   │   └── Role.java                 ← Enum: USER, DECOR, ADMIN
    │   │   ├── repository/                   ← JpaRepository + JPQL + derived queries
    │   │   ├── security/
    │   │   │   ├── JwtUtil.java              ← Token generation & validation
    │   │   │   ├── JwtAuthFilter.java        ← OncePerRequestFilter
    │   │   │   └── CustomUserDetailsService.java
    │   │   └── service/                      ← Business logic layer
    │   └── resources/
    │       ├── application.properties
    │       └── logback-spring.xml
    └── test/
        ├── java/com/grihom/backend/
        │   ├── AuthServiceTest.java
        │   └── BackendApplicationTests.java
        └── resources/
            └── application-test.properties
```

---

## Prerequisites

| Tool | Version |
|---|---|
| Java | 17+ |
| Maven | 3.8+ |
| MySQL | 8.0+ |

---

## Setup & Run

### 1. Create MySQL database
```sql
CREATE DATABASE s54;
```
*(Hibernate auto-creates all tables on first boot)*

### 2. Run with Maven
```bash
cd backend
./mvnw clean install -DskipTests
./mvnw spring-boot:run
```

### 3. Or import into STS
- File → Import → Existing Maven Project → select `backend/`
- Right-click → Run As → Spring Boot App

Backend starts at: **http://localhost:8080**

---

## Default Seeded Credentials

| Role | Email | Password |
|---|---|---|
| ADMIN | admin@grihom.com | admin123 |
| DECOR | decor@grihom.com | decor123 |

---

## API Endpoints

### Auth (Public)
| Method | URL | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user → returns JWT |
| POST | `/api/auth/login` | Login → returns JWT |

### Improvements (Partially Public)
| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/improvements` | Public | All improvements (filterable) |
| GET | `/api/improvements/{id}` | Public | Single improvement |
| POST | `/api/improvements` | ADMIN, DECOR | Create improvement |
| PUT | `/api/improvements/{id}` | ADMIN, DECOR | Update improvement |
| DELETE | `/api/improvements/{id}` | ADMIN, DECOR | Delete improvement |
| GET | `/api/improvements/history` | ADMIN, DECOR | Change history |
| POST | `/api/improvements/history` | ADMIN, DECOR | Log change |

### Reports (Authenticated)
| Method | URL | Description |
|---|---|---|
| GET | `/api/reports` | My reports |
| POST | `/api/reports` | Save report |
| DELETE | `/api/reports/{id}` | Delete my report |

### Planned Improvements (Authenticated)
| Method | URL | Description |
|---|---|---|
| GET | `/api/planned` | My planned improvements |
| POST | `/api/planned` | Add to plan |
| DELETE | `/api/planned/{improvementId}` | Remove from plan |

### Admin (ADMIN / DECOR)
| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/admin/users` | ADMIN, DECOR | List all users |
| PUT | `/api/admin/users/{id}/role` | ADMIN only | Change role |
| PUT | `/api/admin/users/{id}/status` | ADMIN, DECOR | Toggle active |
| DELETE | `/api/admin/users/{id}` | ADMIN only | Delete user |

### Files
| Method | URL | Auth | Description |
|---|---|---|---|
| POST | `/api/files/upload` | ADMIN, DECOR | Upload image |
| GET | `/api/files/{filename}` | Public | Serve file |

### Other
| Method | URL | Auth | Description |
|---|---|---|---|
| GET | `/api/test` | Public | Health check |
| GET | `/api/reviews` | Public | All reviews |
| POST | `/api/reviews` | Optional | Submit review |
| GET | `/actuator/health` | Public | Actuator health |

---

## JWT Flow
1. Client sends `POST /api/auth/login` with `{email, password}`
2. Server validates credentials, returns `{token, id, name, email, role, ...}`
3. Client stores token in `localStorage["grihom_token"]`
4. All subsequent requests include: `Authorization: Bearer <token>`
5. `JwtAuthFilter` validates token → sets `SecurityContext`
6. Controllers access current user via `Authentication auth → auth.getName()` (email)

---

## Running Tests
```bash
./mvnw test
```
Tests use H2 in-memory database (profile: `test`).
