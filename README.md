## Web-Based Barangay Information Management System (MVC PHP)

This is a simple web-based Barangay Information Management System built with a custom PHP MVC structure.

### Features

- **Dashboard**: Summary cards for residents, households, and officials.
- **Residents module**: List of registered residents.
- **Households module**: List of households.
- **Officials module**: List of barangay officials.
- **Authentication**: Basic login/logout for an admin user.

### Requirements

- **PHP**: 8.1 or higher
- **Web server**: Apache or Nginx (or PHP built-in server for development)
- **Database**: MySQL or MariaDB
- **Composer**: for dependency management

### Installation

1. **Install PHP dependencies**

   ```bash
   cd Web-Based-Barangay-Information-Management-System
   composer install
   ```

2. **Configure environment**

   ```bash
   copy .env.example .env    # Windows (PowerShell: cp .env.example .env)
   ```

   Edit `.env` and set your database connection:

   - `DB_HOST`
   - `DB_PORT`
   - `DB_DATABASE`
   - `DB_USERNAME`
   - `DB_PASSWORD`

3. **Create database and tables**

   - Import `database/schema.sql` into your MySQL/MariaDB server.
   - This will:
     - Create the `barangay_db` database (if not existing)
     - Create `users`, `residents`, `households`, and `officials` tables
     - Seed one admin user:
       - **Username**: `admin`
       - **Password**: `admin123`

4. **Run the application (development)**

   ```bash
   php -S localhost:8000 -t public
   ```

   Then open `http://localhost:8000` in your browser.

### Default Routes (MVC)

- **GET /** → `DashboardController@index`
- **GET /residents** → `ResidentController@index`
- **GET /households** → `HouseholdController@index`
- **GET /officials** → `OfficialController@index`
- **GET /login** → `AuthController@loginForm`
- **POST /login** → `AuthController@login`
- **GET /logout** → `AuthController@logout`

### Notes

- This project is intentionally simple to demonstrate the MVC structure:
  - `app/Core` – core classes (router, base controller, base model, database).
  - `app/Controllers` – controllers.
  - `app/Models` – models.
  - `app/Views` – Blade-like PHP templates.
- You can extend it with CRUD operations (create/edit/delete residents, households, officials), audit logs, reports, etc.

