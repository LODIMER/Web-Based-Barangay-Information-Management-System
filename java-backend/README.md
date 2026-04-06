## Java Backend + Web App (Spring Boot)

This folder contains the Java-only rewrite for the Barangay system:

- REST APIs (`/api/...`)
- Thymeleaf web pages (`/login`, `/register`, `/`, `/profile`, `/ayuda/request`, `/schedule`)

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
- `GET /api/blotter` - list recent blotter reports
- `POST /api/blotter` - create blotter report
- `PATCH /api/blotter/{id}/status?status=APPROVED` - update blotter status (official/admin only)

### Implemented Web Pages

- `GET /login` - resident login page
- `GET /register` - resident register page
- `GET /login/official` - official login page
- `GET /register/official` - official register page
- `GET /` - role-aware dashboard
- `GET /profile` - profile page (edit info + upload valid ID)
- `GET /ayuda/request` - resident request / official add ayuda form
- `GET /schedule` - schedules page
- `GET /blotter` - blotter page (submit + view recent reports)
  - Includes status filter (All/Pending/Approved/Resolved/Rejected) and search by type/location/details
  - Includes pagination controls (previous/next)

### Run

1. Update `src/main/resources/application.properties` with your MySQL credentials.
2. In terminal:

   ```bash
   cd java-backend
   mvn spring-boot:run
   ```

3. App runs at `http://localhost:8080`.

### Browser URLs

- Resident login: `http://localhost:8080/login`
- Resident register: `http://localhost:8080/register`
- Official login: `http://localhost:8080/login/official`
- Official register: `http://localhost:8080/register/official`
- Blotter: `http://localhost:8080/blotter`

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
- Resident number is auto-generated in resident registration (`RES-<userId>`).
- Official/admin ayuda creation auto-syncs to schedule when preferred date is provided.

