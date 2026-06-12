-- Student Management System Database Schema (MySQL only)
-- NOTE: The app defaults to SQLite (auto-created). This file is only needed
--       if you switch src/com/studentms/util/DBConnection.java to MySQL.
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'STAFF') DEFAULT 'STAFF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Departments table
CREATE TABLE IF NOT EXISTS departments (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL UNIQUE,
    dept_head VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender ENUM('Male', 'Female', 'Other'),
    address TEXT,
    dept_id INT,
    enrollment_date DATE DEFAULT (CURRENT_DATE),
    gpa DECIMAL(3,2) DEFAULT 0.00,
    status ENUM('Active', 'Inactive', 'Graduated', 'Suspended') DEFAULT 'Active',
    photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- Sample data
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'Administrator', 'ADMIN'),
('staff', 'staff123', 'John Staff', 'STAFF');

INSERT INTO departments (dept_name, dept_head) VALUES
('Computer Science', 'Dr. Alan Turing'),
('Mathematics', 'Dr. Ada Lovelace'),
('Physics', 'Dr. Albert Einstein'),
('Engineering', 'Dr. Nikola Tesla'),
('Business', 'Dr. Peter Drucker');

INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES
('CS2024001', 'Alice', 'Johnson', 'alice.j@uni.edu', '555-0101', '2002-03-15', 'Female', '123 Main St, Springfield', 1, 3.85, 'Active'),
('CS2024002', 'Bob', 'Smith', 'bob.s@uni.edu', '555-0102', '2001-07-22', 'Male', '456 Oak Ave, Shelbyville', 1, 3.45, 'Active'),
('MA2024001', 'Charlie', 'Brown', 'charlie.b@uni.edu', '555-0103', '2002-11-08', 'Male', '789 Pine Rd, Capital City', 2, 3.20, 'Active'),
('PH2024001', 'Diana', 'Prince', 'diana.p@uni.edu', '555-0104', '2000-05-30', 'Female', '321 Elm St, Themyscira', 3, 3.92, 'Active'),
('EN2024001', 'Eve', 'Adams', 'eve.a@uni.edu', '555-0105', '2001-09-12', 'Female', '654 Maple Dr, Metro City', 4, 3.67, 'Active'),
('BU2024001', 'Frank', 'Miller', 'frank.m@uni.edu', '555-0106', '2002-01-25', 'Male', '987 Cedar Ln, Fawcett City', 5, 2.95, 'Inactive'),
('CS2024003', 'Grace', 'Lee', 'grace.l@uni.edu', '555-0107', '2003-04-18', 'Female', '147 Birch Blvd, Central City', 1, 3.78, 'Active'),
('MA2024002', 'Henry', 'Wilson', 'henry.w@uni.edu', '555-0108', '2001-12-03', 'Male', '258 Walnut Way, Star City', 2, 3.10, 'Graduated'),
('PH2024002', 'Ivy', 'Chen', 'ivy.c@uni.edu', '555-0109', '2002-08-07', 'Female', '369 Spruce Ct, Coast City', 3, 3.55, 'Active'),
('EN2024002', 'Jack', 'Davis', 'jack.d@uni.edu', '555-0110', '2000-06-14', 'Male', '741 Aspen Pl, Ivy City', 4, 2.80, 'Suspended');
