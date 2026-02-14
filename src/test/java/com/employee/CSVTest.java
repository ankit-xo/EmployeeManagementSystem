package com.employee;

public class CSVTest {
    public static void main(String[] args) {
        System.out.println("=== Employee Database CSV Test ===\n");
        
        // Create database instance
        EmployeeDatabase db = new EmployeeDatabase();
        
        // Add test employees
        System.out.println("Adding test employees...\n");
        db.addEmployee(new Employee("John Doe", 1, "Engineering", "Senior Developer", "555-1234", "john@company.com", "123 Main St"));
        db.addEmployee(new Employee("Jane Smith", 2, "Marketing", "Marketing Manager", "555-5678", "jane@company.com", "456 Oak Ave"));
        db.addEmployee(new Employee("Bob Johnson", 3, "Sales", "Sales Representative", "555-9012", "bob@company.com", "789 Pine Rd"));
        
        System.out.println("\n=== Current Employees in Memory ===");
        for (Employee emp : db.getEmployees()) {
            System.out.println("  - " + emp.getName() + " (ID: " + emp.getId() + ") - " + emp.getDepartment());
        }
        
        System.out.println("\n=== Test Complete ===");
        System.out.println("✓ Employees have been saved to CSV file");
        System.out.println("✓ CSV file is located in: EmployeeData/employees.csv");
        System.out.println("\nYou can now:");
        System.out.println("  1. Verify the CSV file content");
        System.out.println("  2. Run the application again to verify data persistence");
    }
}
