-- Database schema for Web-Based Barangay Information Management System

CREATE DATABASE IF NOT EXISTS barangay_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE barangay_db;

CREATE TABLE IF NOT EXISTS users (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS residents (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(80) NOT NULL,
  last_name VARCHAR(80) NOT NULL,
  middle_name VARCHAR(80) NULL,
  sex ENUM('Male', 'Female') NOT NULL,
  birth_date DATE NOT NULL,
  address VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS households (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  head_name VARCHAR(160) NOT NULL,
  address VARCHAR(255) NOT NULL,
  members_count INT UNSIGNED DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS officials (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(160) NOT NULL,
  position VARCHAR(100) NOT NULL,
  term_start DATE NULL,
  term_end DATE NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed demo admin user (password: admin123)
INSERT INTO users (username, password, full_name)
VALUES (
  'admin',
  '$2y$10$JvPRQnqBZ4QuvW3vA3sYGejh9E3RIlnJrped1IovnHgwlHGawEqnW',
  'System Administrator'
)
ON DUPLICATE KEY UPDATE username = username;

