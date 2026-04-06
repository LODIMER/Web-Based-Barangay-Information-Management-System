## Java Backend (Spring Boot)

This folder contains the Java-only backend rewrite starter for the Barangay system.

### Stack

- Java 17
- Spring Boot 3
- Spring Web + Spring Data JPA + Spring Security
- MySQL

### Implemented APIs

- `POST /api/auth/register/resident` - create resident account (auto-generates `residentNumber` like `RES-001`)
- `POST /api/auth/register/official` - create official account
- `POST /api/auth/login` - resident/general login
- `POST /api/auth/login/official` - official/admin login only
- `POST /api/auth/logout` - logout
- `GET /api/profile` - current user profile (session-based)
- `PUT /api/profile` - update full name, contact number, address
- `POST /api/profile/id-upload` - upload a valid ID (multipart form-data, field name: `file`)
- `POST /api/ayuda` - create ayuda request (official/admin auto-creates schedule when `preferredDate` is set)
- `GET /api/schedules/upcoming` - list upcoming schedules

### Run

1. Update `src/main/resources/application.properties` with your MySQL credentials.
2. In terminal:

   ```bash
   cd java-backend
   mvn spring-boot:run
   ```

3. App runs at `http://localhost:8080`.

### Quick test (PowerShell)

Resident register:

```bash
curl -X POST http://localhost:8080/api/auth/register/resident ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"res1\",\"fullName\":\"Juan Dela Cruz\",\"password\":\"password123\",\"confirmPassword\":\"password123\"}"
```

Login (saves session cookie):

```bash
curl -c cookies.txt -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"res1\",\"password\":\"password123\"}"
```

Get profile:

```bash
curl -b cookies.txt http://localhost:8080/api/profile
```

Upload valid ID:

```bash
curl -b cookies.txt -X POST http://localhost:8080/api/profile/id-upload ^
  -F "file=@C:\path\to\id.png"
```

### Notes

- This is a migration starter to move from PHP to Java.
- `/api/**` is now protected at Spring Security level (except `/api/auth/**`).
- Authentication is session-based for now (cookie-backed).
- Next step: add proper JWT or Spring Security auth filters and build Java frontend/API clients.

