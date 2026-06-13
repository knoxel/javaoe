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
('EN2024002', 'Jack', 'Davis', 'jack.d@uni.edu', '555-0110', '2000-06-14', 'Male', '741 Aspen Pl, Ivy City', 4, 2.80, 'Suspended'),

-- Additional departments
INSERT IGNORE INTO departments (dept_name, dept_head) VALUES
('Biology', 'Dr. Charles Darwin'),
('Chemistry', 'Dr. Marie Curie'),
('Psychology', 'Dr. Sigmund Freud'),
('English Literature', 'Dr. Jane Austen'),
('History', 'Dr. Herodotus');

-- Additional students
INSERT IGNORE INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES
('CS2024004', 'Katherine', 'Martinez', 'katherine.m@uni.edu', '555-0111', '2002-02-28', 'Female', '852 Fir Loop, Central City', 1, 3.91, 'Active'),
('CS2024005', 'Liam', 'Anderson', 'liam.a@uni.edu', '555-0112', '2001-10-05', 'Male', '963 Spruce Dr, Keystone City', 1, 3.32, 'Active'),
('MA2024003', 'Mia', 'Taylor', 'mia.t@uni.edu', '555-0113', '2003-01-19', 'Female', '159 Elmwood Ave, Midway City', 2, 3.60, 'Active'),
('PH2024003', 'Noah', 'Thomas', 'noah.t@uni.edu', '555-0114', '2000-09-27', 'Male', '753 Willow Ln, Opal City', 3, 3.48, 'Active'),
('EN2024003', 'Olivia', 'White', 'olivia.w@uni.edu', '555-0115', '2002-06-11', 'Female', '462 Chestnut St, Hudson City', 4, 3.15, 'Inactive'),
('BU2024002', 'Ethan', 'Harris', 'ethan.h@uni.edu', '555-0116', '2001-04-03', 'Male', '684 Poplar Ct, St. Roch', 5, 3.05, 'Active'),
('BI2024001', 'Sophia', 'Clark', 'sophia.c@uni.edu', '555-0117', '2002-08-14', 'Female', '321 River Rd, Coral City', 6, 3.72, 'Active'),
('BI2024002', 'Aiden', 'Lewis', 'aiden.l@uni.edu', '555-0118', '2001-12-22', 'Male', '555 Lake Dr, Pacific City', 6, 3.28, 'Active'),
('CH2024001', 'Emma', 'Walker', 'emma.w@uni.edu', '555-0119', '2003-03-09', 'Female', '777 Hill St, Ivy Town', 7, 3.85, 'Active'),
('CH2024002', 'Logan', 'Hall', 'logan.h@uni.edu', '555-0120', '2000-07-30', 'Male', '888 Valley Ave, Bludhaven', 7, 3.40, 'Graduated'),
('PS2024001', 'Ava', 'Young', 'ava.y@uni.edu', '555-0121', '2002-11-16', 'Female', '222 Sunset Blvd, Gotham', 8, 3.55, 'Active'),
('PS2024002', 'Lucas', 'King', 'lucas.k@uni.edu', '555-0122', '2001-05-20', 'Male', '444 Moon Ct, Metropolis', 8, 2.90, 'Suspended'),
('EN2024004', 'Isabella', 'Scott', 'isabella.s@uni.edu', '555-0123', '2003-09-02', 'Female', '111 Star Ave, Smallville', 9, 3.98, 'Active'),
('HI2024001', 'Mason', 'Green', 'mason.g@uni.edu', '555-0124', '2000-04-12', 'Male', '666 Harbor Way, Happy Harbor', 10, 3.10, 'Active'),
('HI2024002', 'Ella', 'Baker', 'ella.b@uni.edu', '555-0125', '2001-10-28', 'Female', '999 Meadow Ln, Gateway City', 10, 3.35, 'Active'),
('BU2024003', 'James', 'Nelson', 'james.n@uni.edu', '555-0126', '2002-07-07', 'Male', '333 Park Ave, Fawcett City', 5, 2.75, 'Inactive');
