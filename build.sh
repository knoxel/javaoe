#!/bin/bash
# Build script for Student Management System
# Requirements: JDK 8+, MySQL 5.7+, MySQL Connector/J 8.x

set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
BUILD_DIR="$PROJECT_DIR/build"
LIB_DIR="$PROJECT_DIR/lib"
DIST_DIR="$PROJECT_DIR/dist"

echo "==========================================="
echo "  Student Management System - Build Script"
echo "==========================================="

# Clean
echo "[1/4] Cleaning build artifacts..."
rm -rf "$BUILD_DIR" "$DIST_DIR"
mkdir -p "$BUILD_DIR/classes" "$DIST_DIR"

# Check for MySQL connector
if [ ! -f "$LIB_DIR/mysql-connector-j-*.jar" ]; then
    echo ""
    echo "WARNING: MySQL Connector/J not found in $LIB_DIR"
    echo "Download from: https://dev.mysql.com/downloads/connector/j/"
    echo "Place the JAR file in: $LIB_DIR/"
    echo ""
    echo "Attempting to compile without JDBC driver (will compile but won't connect to DB)..."
    echo ""
fi

# Compile
echo "[2/4] Compiling Java sources..."
find "$SRC_DIR" -name "*.java" > "$BUILD_DIR/sources.txt"
javac -d "$BUILD_DIR/classes" -sourcepath "$SRC_DIR" @"$BUILD_DIR/sources.txt" 2>&1

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi
echo "  Compilation successful!"

# Create manifest
echo "[3/4] Creating JAR manifest..."
cat > "$BUILD_DIR/MANIFEST.MF" << 'EOF'
Manifest-Version: 1.0
Main-Class: com.studentms.App
Class-Path: lib/mysql-connector-j-8.0.33.jar
EOF

# Create JAR
echo "[4/4] Creating JAR file..."
cd "$BUILD_DIR/classes"
jar cfm "$DIST_DIR/StudentMS.jar" "$BUILD_DIR/MANIFEST.MF" com/
cd "$PROJECT_DIR"

# Copy libs
cp "$LIB_DIR"/*.jar "$DIST_DIR/" 2>/dev/null || true

echo ""
echo "==========================================="
echo "  Build Complete!"
echo "==========================================="
echo ""
echo "  JAR: $DIST_DIR/StudentMS.jar"
echo ""
echo "  To run:"
echo "    cd $DIST_DIR"
echo "    java -jar StudentMS.jar"
echo ""
echo "  Prerequisites:"
echo "    1. MySQL Server running on localhost:3306"
echo "    2. Run sql/schema.sql to create database"
echo "    3. Default login: admin / admin123"
echo ""
