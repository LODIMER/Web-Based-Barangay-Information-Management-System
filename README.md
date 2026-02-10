## Web-Based Barangay Information Management System (MVC PHP)

This is a web-based Barangay Information Management System built with a custom PHP MVC structure.
It includes a resident portal and a barangay official/admin area.

### Features

- **Resident dashboard**:
  - Calendar-style view of upcoming ayuda schedules.
  - Notifications area for request/status updates.
- **Residents module**: List of registered residents.
- **Households module**: List of households.
- **Officials module**: List of barangay officials.
- **Resident portal**:
  - Resident login/registration (role `resident`).
  - Auto-generated **Resident Number** (stored in the `users.resident_number` column and visible on the profile page).
  - Profile page where residents can edit their info and upload a valid ID for verification.
  - Request Ayuda form.
- **Barangay official/admin portal**:
  - Separate **official login** and registration (roles `official` and `admin`).
  - Navbar actions **Add Ayuda** and **Add Schedule**.
  - Adding an Ayuda with a preferred date automatically creates a linked schedule entry.
- **Authentication**:
  - Session-based login/logout.
  - Role-aware navigation and access control (resident vs official/admin).

### Requirements

- **PHP**: 8.1 or higher
- **Web server**: Apache or Nginx (or XAMPP for local development)
- **Database**: MySQL or MariaDB

### Installation (Step by Step for Beginners)

#### 1. Put the project in XAMPP

1. Close any running PHP dev server.
2. Copy this folder (`Web-Based-Barangay-Information-Management-System`) into your XAMPP `htdocs` directory, for example:  
   `F:\XAMPP\htdocs\Web-Based-Barangay-Information-Management-System`

#### 2. Create the database

1. Open **XAMPP Control Panel** and start **Apache** and **MySQL**.
2. Open your browser and go to `http://localhost/phpmyadmin`.
3. Click **Databases** and create a new database, e.g. `barangay_database` (or match the name you will put in `.env`).
4. With that database selected, go to the **Import** tab.
5. Choose the file `database/schema.sql` from this project and click **Go**.
6. This creates all required tables (`users`, `residents`, `households`, `officials`, `ayuda_requests`, `schedules`, etc.) and seeds an admin user:
   - **Username**: `admin`
   - **Password**: `admin123`

#### 3. Configure the `.env` file

1. In the project root, duplicate the example file:

   ```bash
   copy .env.example .env    # Windows (PowerShell: cp .env.example .env)
   ```

2. Open `.env` in a text editor and update the values:
   - `DB_HOST=127.0.0.1`
   - `DB_PORT=3306`
   - `DB_DATABASE=barangay_database`   ← match the name you created in phpMyAdmin
   - `DB_USERNAME=root`               ← default XAMPP username
   - `DB_PASSWORD=`                   ← leave empty if you didn’t set a MySQL password

#### 4. Run the app

You have two options:

- **Option A – Via PHP built-in server (for non-XAMPP setups)**

  ```bash
  php -S localhost:8000 -t public
  ```

  Then open `http://localhost:8000` in your browser.

- **Option B – Via XAMPP Apache (recommended if you’re already using XAMPP)**

1. Make sure Apache is running in XAMPP.
2. In your browser, go to:  
   `http://localhost/Web-Based-Barangay-Information-Management-System/`
3. The app will route everything through `public/index.php` automatically.

### Key Routes

- **Resident / portal**
  - `GET /` → `DashboardController@index` (role-aware dashboard).
  - `GET /login` → `AuthController@loginForm`
  - `POST /login` → `AuthController@login`
  - `GET /register` → `AuthController@registerForm`
  - `POST /register` → `AuthController@register`
  - `GET /profile` → `ProfileController@show`
  - `POST /profile` → `ProfileController@update`
  - `GET /ayuda/request` → `AyudaRequestController@create`
  - `POST /ayuda/request` → `AyudaRequestController@store`

- **Barangay officials/admin**
  - `GET /login/official` → `AuthController@officialLoginForm`
  - `POST /login/official` → `AuthController@officialLogin`
  - `GET /register/official` → `AuthController@officialRegisterForm`
  - `POST /register/official` → `AuthController@officialRegister`
  - `GET /schedule` → `ScheduleController@index` (shows schedules created from Ayuda with preferred dates)
  - `GET /residents` → `ResidentController@index`
  - `GET /households` → `HouseholdController@index`
  - `GET /officials` → `OfficialController@index`

- **Common**
  - `GET /logout` → `AuthController@logout`

### URLs when running under XAMPP

If the project folder is `Web-Based-Barangay-Information-Management-System` inside `htdocs`, use these URLs in your browser:

- **Resident**
  - Dashboard:  
    `http://localhost/Web-Based-Barangay-Information-Management-System/`
  - Login:  
    `http://localhost/Web-Based-Barangay-Information-Management-System/login`
  - Register:  
    `http://localhost/Web-Based-Barangay-Information-Management-System/register`

- **Barangay Official / Admin**
  - Official login:  
    `http://localhost/Web-Based-Barangay-Information-Management-System/login/official`
  - Official register:  
    `http://localhost/Web-Based-Barangay-Information-Management-System/register/official`

### Notes

- Core MVC structure:
  - `app/Core` – router, base controller, base model, database wrapper.
  - `app/Controllers` – controllers.
  - `app/Models` – models.
  - `app/Views` – PHP templates.
- You can extend it with full CRUD, approval workflows for ayuda requests, resident verification, reports, etc.

