## Barangay IMS - Java Version (Beginner Guide)

This is the Java version of the system using Spring Boot + MySQL.

It includes:
- Resident login and registration
- Official login and registration
- Profile update + valid ID upload
- Ayuda requests (official can auto-create schedule)
- Schedule page
- Blotter page (submit report, filter/search, status update for officials/admin)

---

## 5-minute quick start

If you already have Java, Maven, and MySQL installed, do this:

1. Open terminal in `java-backend`
2. Create the database:

   ```sql
   CREATE DATABASE IF NOT EXISTS barangay_database;
   ```

3. Check DB credentials in `src/main/resources/application.properties`
4. Run:

   ```bash
   mvn spring-boot:run
   ```

5. Open:
   - `http://localhost:8080/register` (resident register), or
   - `http://localhost:8080/register/official` (official register)

---

## How the system works (simple summary)

1. A user registers as **Resident** or **Official**.
2. User logs in and gets a session (keeps them signed in).
3. Residents can request ayuda and submit blotter reports.
4. Officials/admin can create ayuda entries with a preferred date; that date is also saved in schedule.
5. Officials/admin can update blotter status:
   - `PENDING`
   - `APPROVED`
   - `RESOLVED`
   - `REJECTED`

Resident accounts automatically get a resident number like `RES-001`.

---

## Requirements

Install these first:

- Java 17 or higher
- Maven 3.9+ (or any recent Maven)
- MySQL (or MariaDB)

---

## Step-by-step setup (first time)

### 1) Open this folder in terminal

```bash
cd java-backend
```

### 2) Create database in MySQL

Open MySQL (or phpMyAdmin) and create:

```sql
CREATE DATABASE IF NOT EXISTS barangay_database;
```

### 3) Configure database connection

Open:

`src/main/resources/application.properties`

Make sure these values are correct for your PC:

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/barangay_database
spring.datasource.username=root
spring.datasource.password=
```

If your MySQL has a password, put it in `spring.datasource.password`.

### 4) Run the app

```bash
mvn spring-boot:run
```

When it starts, open:

`http://localhost:8080/login`

---

## Main URLs

- Resident login: `http://localhost:8080/login`
- Resident register: `http://localhost:8080/register`
- Official login: `http://localhost:8080/login/official`
- Official register: `http://localhost:8080/register/official`
- Dashboard: `http://localhost:8080/`
- Profile: `http://localhost:8080/profile`
- Ayuda form: `http://localhost:8080/ayuda/request`
- Schedule: `http://localhost:8080/schedule`
- Blotter: `http://localhost:8080/blotter`

---

## API (optional, for testing/Postman)

- `POST /api/auth/register/resident`
- `POST /api/auth/register/official`
- `POST /api/auth/login`
- `POST /api/auth/login/official`
- `POST /api/auth/logout`
- `GET /api/profile`
- `PUT /api/profile`
- `POST /api/profile/id-upload` (multipart, field name: `file`)
- `POST /api/ayuda`
- `GET /api/schedules/upcoming`
- `GET /api/blotter`
- `POST /api/blotter`
- `PATCH /api/blotter/{id}/status?status=APPROVED`

---

## Troubleshooting

- **Port 8080 already in use**
  - Stop the app using that port, or change `server.port` in `application.properties`.

- **Access denied for MySQL user**
  - Check `spring.datasource.username` and `spring.datasource.password`.

- **Database/table errors**
  - Confirm `barangay_database` exists and app has permission.
  - Restart app after fixing DB config.

- **Maven command not found**
  - Install Maven and reopen terminal.


