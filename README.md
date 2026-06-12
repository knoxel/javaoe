# javaoe — Student Management System

A desktop application built with **Java Swing** and **JDBC** for managing student records. Uses **SQLite** as the embedded database (no server setup required).

## Features

- **Login** — role-based authentication (Admin / Staff)
- **Dashboard** — statistics overview (total/active students, average GPA, department counts)
- **Student Management** — add, edit, view, delete student records
- **Search** — filter students by name, roll number, or email
- **Departments** — view department listings with heads
- **Modern UI** — custom color theme, sidebar navigation, styled tables and buttons

## Prerequisites

- **Java 8+** (tested on Java 17)
- **curl** (for automatic JDBC driver download)

## Quick Start

```bash
# Clone
git clone https://github.com/knoxel/javaoe.git
cd javaoe

# Build and run
./build.sh
java -jar dist/StudentMS.jar
```

That's it — the SQLite database is auto-created at `~/StudentMS/studentms.db` and seeded with sample data on first launch.

## Login Credentials

| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin123 | ADMIN |
| staff    | staff123 | STAFF |

## Manual Compilation (without build script)

```bash
# Download SQLite JDBC driver
mkdir -p lib
curl -L -o lib/sqlite-jdbc-3.42.0.0.jar \
  https://github.com/xerial/sqlite-jdbc/releases/download/3.42.0.0/sqlite-jdbc-3.42.0.0.jar

# Compile
mkdir -p build/classes
javac -d build/classes -cp "lib/*" -sourcepath src $(find src -name "*.java")

# Run
java -cp "build/classes:lib/*" com.studentms.App
```

## Optional: MySQL instead of SQLite

The app defaults to **SQLite** (embedded, no server). To use **MySQL** instead:

1. Start a MySQL server and run `sql/schema.sql` to create the database
2. Edit `src/com/studentms/util/DBConnection.java` and swap the connection to MySQL
3. Place `mysql-connector-j-8.x.jar` in `lib/`
4. Rebuild

## Project Structure

```
javaoe/
├── build.sh                    # Build & run script
├── sql/schema.sql              # MySQL schema (optional)
├── src/com/studentms/
│   ├── App.java                # Entry point
│   ├── model/                  # Data models (Student, User, Department)
│   ├── dao/                    # Data access layer (JDBC queries)
│   ├── ui/                     # Swing UI (Login, Dashboard, CRUD panels)
│   └── util/                   # DB connection, theme/styling
├── lib/                        # JDBC drivers (auto-downloaded)
├── build/                      # Compiled classes
└── dist/                       # Runnable JAR
```
