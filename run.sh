#!/bin/bash

set -e

echo "Compiling Employee Management System..."
mkdir -p build/classes
javac -cp "lib/*" -d build/classes $(find src/main/java -name "*.java")

echo "Compilation successful!"
echo "Starting Employee Management System..."

if java -cp "build/classes:lib/*" com.employee.EmployeeFrontendUI; then
    exit 0
fi

echo "GUI launch failed in this environment."
echo "Running headless CSV smoke test instead..."
mkdir -p build/test-classes
javac -cp "lib/*:build/classes" -d build/test-classes src/test/java/com/employee/CSVTest.java
java -cp "build/classes:build/test-classes:lib/*" com.employee.CSVTest
