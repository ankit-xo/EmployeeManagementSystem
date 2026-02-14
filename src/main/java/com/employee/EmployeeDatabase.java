package com.employee;

/*
 * Maintainer reference:
 * GitHub: https://github.com/ankit-xo
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmployeeDatabase {
    private List<Employee> employees = new ArrayList<>();
    private final String folderPath;
    private final String filePath;

    public EmployeeDatabase() {
        // Use absolute path based on project directory
        String projectDir = System.getProperty("user.dir");
        this.folderPath = projectDir + File.separator + "EmployeeData";
        this.filePath = folderPath + File.separator + "employees.csv";
        
        System.out.println("📦 Initializing Employee Database...");
        System.out.println("📂 Data folder: " + folderPath);
        System.out.println("📝 Data file: " + filePath);
        createFolderIfNotExists();
        
        // Verify folder was created
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println("✓ Folder created successfully");
        }
        
        loadEmployees();
    }

    private void createFolderIfNotExists() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("✓ Created EmployeeData folder");
            }
        }
    }

    public boolean addEmployee(Employee employee) {
        if (getEmployeeById(employee.getId()) != null) {
            return false;
        }
        System.out.println("Adding employee: " + employee);
        employees.add(employee);
        saveEmployees();
        System.out.println("Employee added and saved.");
        return true;
    }

    public boolean updateEmployee(int id, String newName, String newDepartment, String newPosition, String newContact, String newEmail, String newAddress) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                employees.set(i, new Employee(newName, id, newDepartment, newPosition, newContact, newEmail, newAddress));
                saveEmployees();
                return true;
            }
        }
        return false;
    }

    public boolean updateEmployee(int id, Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                employees.set(i, employee);
                saveEmployees();
                return true;
            }
        }
        return false;
    }

    public List<Employee> getEmployees() { 
        return employees; 
    }

    public Employee getEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    public boolean deleteEmployee(int id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                employees.remove(i);
                saveEmployees();
                return true;
            }
        }
        return false;
    }

    // Public method to manually save employees (called on app exit)
    public void saveEmployeesManually() {
        saveEmployees();
    }

    private void saveEmployees() {
        try {
            createFolderIfNotExists();
            
            System.out.println("\n💾 SAVING: " + employees.size() + " employees to: " + filePath);
            
            File file = new File(filePath);
            try (FileWriter fw = new FileWriter(file);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                
                // Write header row
                String[] headers = {"ID", "Name", "Department", "Position", "Contact", "Email", "Address"};
                bw.write(String.join(",", headers));
                bw.newLine();
                
                // Write data rows
                int rowNum = 1;
                for (Employee emp : employees) {
                    String line = String.format("%d,%s,%s,%s,%s,%s,%s",
                            emp.getId(),
                            escapeCSV(emp.getName()),
                            escapeCSV(emp.getDepartment()),
                            escapeCSV(emp.getPosition()),
                            escapeCSV(emp.getContact()),
                            escapeCSV(emp.getEmail()),
                            escapeCSV(emp.getAddress())
                    );
                    bw.write(line);
                    bw.newLine();
                    System.out.println("   ➕ Row " + rowNum + ": " + emp.getName());
                    rowNum++;
                }
            }
            
            System.out.println("✓ Employee data saved successfully!");
            System.out.println("📊 File size: " + file.length() + " bytes\n");
        } catch (IOException e) {
            System.err.println("✗ Error saving employee data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        // If the value contains comma, quote, or newline, wrap it in quotes and escape internal quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private void loadEmployees() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("ℹ No existing employee data found. Starting fresh.");
            return;
        }
        
        System.out.println("\n📖 LOADING employees from: " + filePath);
        System.out.println("📊 File size: " + file.length() + " bytes");
        
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            
            String line;
            int lineNum = 0;
            int loadedCount = 0;
            Set<Integer> seenIds = new HashSet<>();
            
            while ((line = br.readLine()) != null) {
                lineNum++;
                
                // Skip header row
                if (lineNum == 1) {
                    continue;
                }
                
                try {
                    String[] values = parseCSVLine(line);
                    
                    if (values.length < 7) {
                        System.err.println("⚠ Skipped invalid row at line " + lineNum + " (not enough columns)");
                        continue;
                    }
                    
                    int id = Integer.parseInt(values[0].trim());
                    if (seenIds.contains(id)) {
                        System.err.println("⚠ Skipped duplicate employee ID at line " + lineNum + ": " + id);
                        continue;
                    }
                    String name = values[1].trim();
                    String department = values[2].trim();
                    String position = values[3].trim();
                    String contact = values[4].trim();
                    String email = values[5].trim();
                    String address = values[6].trim();
                    
                    employees.add(new Employee(name, id, department, position, contact, email, address));
                    seenIds.add(id);
                    System.out.println("   ➕ Loaded: " + name + " (ID: " + id + ")");
                    loadedCount++;
                } catch (Exception e) {
                    System.err.println("⚠ Skipped invalid row at line " + lineNum + ": " + e.getMessage());
                }
            }
            System.out.println("✓ Loaded " + loadedCount + " employees\n");
        } catch (IOException e) {
            System.err.println("✗ Error loading employee data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Handle escaped quote
                    current.append('"');
                    i++;
                } else {
                    // Toggle quote state
                    insideQuotes = !insideQuotes;
                }
            } else if (c == ',' && !insideQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        values.add(current.toString());
        return values.toArray(new String[0]);
    }
}
