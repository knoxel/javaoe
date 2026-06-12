package com.studentms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DBConnection {
    private static final String DB_DIR = System.getProperty("user.home") + File.separator + "StudentMS";
    private static final String DB_PATH = DB_DIR + File.separator + "studentms.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            new File(DB_DIR).mkdirs();
            initDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC Driver not found. Add sqlite-jdbc to lib/.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database.", e);
        }
    }

    private static void initDatabase() throws SQLException {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute("PRAGMA journal_mode=WAL");
            st.execute("PRAGMA foreign_keys=ON");

            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "full_name TEXT NOT NULL," +
                "role TEXT DEFAULT 'STAFF'," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            st.execute("CREATE TABLE IF NOT EXISTS departments (" +
                "dept_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dept_name TEXT NOT NULL UNIQUE," +
                "dept_head TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            st.execute("CREATE TABLE IF NOT EXISTS students (" +
                "student_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "roll_number TEXT NOT NULL UNIQUE," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "email TEXT UNIQUE," +
                "phone TEXT," +
                "date_of_birth DATE," +
                "gender TEXT," +
                "address TEXT," +
                "dept_id INTEGER," +
                "enrollment_date DATE DEFAULT (DATE('now'))," +
                "gpa REAL DEFAULT 0.00," +
                "status TEXT DEFAULT 'Active'," +
                "photo_path TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL)");

            // Seed data if empty
            java.sql.ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users");
            rs.next();
            if (rs.getInt(1) == 0) {
                st.execute("INSERT INTO users (username, password, full_name, role) VALUES ('admin', 'admin123', 'Administrator', 'ADMIN')");
                st.execute("INSERT INTO users (username, password, full_name, role) VALUES ('staff', 'staff123', 'John Staff', 'STAFF')");

                st.execute("INSERT INTO departments (dept_name, dept_head) VALUES ('Computer Science', 'Dr. Alan Turing')");
                st.execute("INSERT INTO departments (dept_name, dept_head) VALUES ('Mathematics', 'Dr. Ada Lovelace')");
                st.execute("INSERT INTO departments (dept_name, dept_head) VALUES ('Physics', 'Dr. Albert Einstein')");
                st.execute("INSERT INTO departments (dept_name, dept_head) VALUES ('Engineering', 'Dr. Nikola Tesla')");
                st.execute("INSERT INTO departments (dept_name, dept_head) VALUES ('Business', 'Dr. Peter Drucker')");

                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('CS2024001', 'Alice', 'Johnson', 'alice.j@uni.edu', '555-0101', '2002-03-15', 'Female', '123 Main St, Springfield', 1, 3.85, 'Active')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('CS2024002', 'Bob', 'Smith', 'bob.s@uni.edu', '555-0102', '2001-07-22', 'Male', '456 Oak Ave, Shelbyville', 1, 3.45, 'Active')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('MA2024001', 'Charlie', 'Brown', 'charlie.b@uni.edu', '555-0103', '2002-11-08', 'Male', '789 Pine Rd, Capital City', 2, 3.20, 'Active')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('PH2024001', 'Diana', 'Prince', 'diana.p@uni.edu', '555-0104', '2000-05-30', 'Female', '321 Elm St, Themyscira', 3, 3.92, 'Active')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('EN2024001', 'Eve', 'Adams', 'eve.a@uni.edu', '555-0105', '2001-09-12', 'Female', '654 Maple Dr, Metro City', 4, 3.67, 'Active')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('BU2024001', 'Frank', 'Miller', 'frank.m@uni.edu', '555-0106', '2002-01-25', 'Male', '987 Cedar Ln, Fawcett City', 5, 2.95, 'Inactive')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('CS2024003', 'Grace', 'Lee', 'grace.l@uni.edu', '555-0107', '2003-04-18', 'Female', '147 Birch Blvd, Central City', 1, 3.78, 'Active')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('MA2024002', 'Henry', 'Wilson', 'henry.w@uni.edu', '555-0108', '2001-12-03', 'Male', '258 Walnut Way, Star City', 2, 3.10, 'Graduated')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('PH2024002', 'Ivy', 'Chen', 'ivy.c@uni.edu', '555-0109', '2002-08-07', 'Female', '369 Spruce Ct, Coast City', 3, 3.55, 'Active')");
                st.execute("INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) VALUES ('EN2024002', 'Jack', 'Davis', 'jack.d@uni.edu', '555-0110', '2000-06-14', 'Male', '741 Aspen Pl, Ivy City', 4, 2.80, 'Suspended')");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
