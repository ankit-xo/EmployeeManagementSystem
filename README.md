# Employee Management System

A Java Swing desktop application for managing employee records with CSV-based persistence.

## Features

- Full-screen desktop UI on launch
- Curved, modern form/table UI
- Add, update, delete, and view employee by ID
- Search employee directory by:
  - Name
  - ID
  - Department
  - Position
- Input validation:
  - Required fields
  - Email format
  - 10-digit contact number
- Duplicate employee ID prevention
- CSV duplicate-ID cleanup while loading existing data
- Auto-save employee data to `EmployeeData/employees.csv`

## Project Structure

```text
src/main/java/com/employee/EmployeeFrontendUI.java
src/main/java/com/employee/EmployeeDatabase.java
src/main/java/com/employee/Employee.java
src/test/java/com/employee/CSVTest.java
run.sh
requirements.txt
EmployeeData/employees.csv
```

## Run

### One-line command

```bash
cd "/Users/ankit/Documents/Coding Project/EmployeeManagementSystem" && chmod +x run.sh && ./run.sh
```

### Standard run

```bash
chmod +x run.sh
./run.sh
```

`run.sh` does:
1. Compile all Java sources from `src/main/java`
2. Start GUI main class: `com.employee.EmployeeFrontendUI`
3. If GUI is unavailable (headless environment), run `CSVTest` fallback

## Manual Compile and Run

```bash
mkdir -p build/classes
javac -cp "lib/*" -d build/classes $(find src/main/java -name "*.java")
java -cp "build/classes:lib/*" com.employee.EmployeeFrontendUI
```

## How to Use

1. Open **Employee Form** tab.
2. Enter employee details.
3. Use actions:
   - **Add**
   - **Update**
   - **Delete**
   - **View**
   - **Clear**
4. Open **Employee Directory** tab to search/filter records.

## Data File

- All records are stored in: `EmployeeData/employees.csv`

## Requirements

- Java JDK 11 or higher
- Shell: `bash` or `zsh`
- Dependencies are already included in `lib/`

Note: This is **not** a Python project. `requirements.txt` is informational, not for `pip install`.

