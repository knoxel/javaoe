#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
BUILD_DIR="$PROJECT_DIR/build"
LIB_DIR="$PROJECT_DIR/lib"
DIST_DIR="$PROJECT_DIR/dist"

echo "=========================================="
echo "  Student Management System - Build"
echo "=========================================="

# Download SQLite JDBC if missing
if ! ls "$LIB_DIR"/sqlite-jdbc-*.jar 2>/dev/null | head -1 > /dev/null 2>&1; then
    echo "[1/3] Downloading SQLite JDBC driver..."
    mkdir -p "$LIB_DIR"
    curl -sL -o "$LIB_DIR/sqlite-jdbc-3.42.0.0.jar" \
      "https://github.com/xerial/sqlite-jdbc/releases/download/3.42.0.0/sqlite-jdbc-3.42.0.0.jar"
fi

# Clean & compile
echo "[2/3] Compiling..."
rm -rf "$BUILD_DIR" "$DIST_DIR"
mkdir -p "$BUILD_DIR/classes"
find "$SRC_DIR" -name "*.java" > "$BUILD_DIR/sources.txt"
javac -d "$BUILD_DIR/classes" -cp "$LIB_DIR/*" -sourcepath "$SRC_DIR" @"$BUILD_DIR/sources.txt"

echo "[3/3] Creating JAR..."
mkdir -p "$DIST_DIR"
cp "$LIB_DIR"/*.jar "$DIST_DIR/"
cat > "$BUILD_DIR/MANIFEST.MF" << EOF
Manifest-Version: 1.0
Main-Class: com.studentms.App
Class-Path: $(for j in "$LIB_DIR"/*.jar; do echo -n "lib/$(basename "$j") "; done)
EOF
cd "$BUILD_DIR/classes"
jar cfm "$DIST_DIR/StudentMS.jar" "$BUILD_DIR/MANIFEST.MF" com/
cd "$PROJECT_DIR"

echo ""
echo "  Build complete: $DIST_DIR/StudentMS.jar"
echo ""
echo "  Quick start:"
echo "    java -jar $DIST_DIR/StudentMS.jar"
echo ""
echo "  Login:  admin / admin123"
echo "  DB:     SQLite (auto-created at ~/StudentMS/studentms.db)"
echo ""
