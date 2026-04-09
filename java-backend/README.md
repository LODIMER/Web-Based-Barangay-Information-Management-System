## Barangay IMS - Java Version (Beginner Guide)

This is the Java version of the system using Spring Boot + MySQL.

It includes:
- Resident login and registration
- Official login/registration with approval workflow
- Profile update + valid ID upload (official-managed)
- Ayuda requests (official can auto-create schedule)
- Ayuda requests with `PENDING` -> `APPROVED` flow (official approval only)
- Schedule page
- Blotter page (submit report, resident update request, status update for approved officials)

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
   - `http://localhost:8080/register` (choose Resident or Barangay Official)

---

## How the system works (simple summary)

1. A user registers as **Resident** or **Official**.
2. The system ensures a first default approved official exists (seeded at startup if needed).
3. New official registrations are `pending` and must be approved by an approved official.
4. User logs in and gets a session (keeps them signed in).
5. Residents can request ayuda and submit blotter reports.
6. Residents can request follow-up updates on their own blotter reports.
7. Ayuda requests are created with `PENDING` status.
8. Approved barangay officials can approve ayuda requests (`APPROVED`), and if a preferred date exists, it is synced to schedule.
9. Approved officials can update blotter status:
   - `PENDING`
   - `APPROVED`
   - `RESOLVED`
   - `REJECTED`

Resident accounts automatically get a resident number like `RES-001`.

### Access rules (current)

- **Resident**
  - Can access: `Blotter`, `Request Ayuda`, `My Profile`.
  - Profile edit is limited to phone number and address.
- **Approved Official**
  - Can approve new official registrations.
  - Can manage residents (including valid ID review/update).
  - Can update blotter status and approve ayuda requests.
  - Can access schedule management pages.

---

## Requirements

Install these first:

- Java 17 or higher
- Maven 3.9+ (or any recent Maven)
- MySQL (or MariaDB)

### Download links

- Java 17 (Temurin): [https://adoptium.net/temurin/releases/?version=17](https://adoptium.net/temurin/releases/?version=17)
- Maven: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
- MySQL Community Server: [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)
- MySQL Workbench (optional GUI): [https://dev.mysql.com/downloads/workbench/](https://dev.mysql.com/downloads/workbench/)
- XAMPP (optional, includes MySQL + phpMyAdmin): [https://www.apachefriends.org/download.html](https://www.apachefriends.org/download.html)

### Quick install notes (Windows)

1. Install **Java 17** and verify:
   ```bash
   java -version
   ```
2. Install **Maven**:
   - Extract Maven zip (for example to `C:\tools\apache-maven-3.9.x`)
   - Add Maven `bin` to PATH:
     - `C:\tools\apache-maven-3.9.x\bin`
   - Open a new terminal and verify:
   ```bash
   mvn -v
   ```
3. Install/start **MySQL** and make sure you can connect with your username/password.

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

### 3.1) (For existing databases) apply migration SQL

If you already have data and want to align schema with latest features, run:

```sql
SOURCE db_migration_official_resident_updates.sql;
```

Or copy/run SQL contents from:

`java-backend/db_migration_official_resident_updates.sql`

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
- Login: `http://localhost:8080/login` (choose Resident or Barangay Official)
- Register: `http://localhost:8080/register` (choose Resident or Barangay Official)
- Dashboard: `http://localhost:8080/`
- Profile: `http://localhost:8080/profile`
- Ayuda form: `http://localhost:8080/ayuda/request`
- Schedule: `http://localhost:8080/schedule`
- Blotter: `http://localhost:8080/blotter`
- Official approvals: `http://localhost:8080/officials/approvals`
- Resident management: `http://localhost:8080/residents`

---

## API (optional, for testing/Postman)

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/profile`
- `PUT /api/profile`
- `POST /api/profile/id-upload` (multipart, field name: `file`)
- `POST /api/ayuda`
- `PATCH /api/ayuda/{id}/approve`
- `GET /api/schedules/upcoming`
- `GET /api/blotter`
- `POST /api/blotter`
- `PATCH /api/blotter/{id}/status?status=APPROVED`
- `PATCH /api/blotter/{id}/request-update?requestMessage=...`
- `POST /api/auth/officials/{officialId}/approve`

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


