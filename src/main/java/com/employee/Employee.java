package com.employee;

public class Employee {
    private String name;
    private int id;
    private String department;
    private String position;
    private String contact;
    private String email;
    private String address;

    public Employee(String name, int id, String department, String position, String contact, String email, String address) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.position = position;
        this.contact = contact;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "\n\n╔══════════════════════════════════════════════════════════════════════════════╗\n" +
               "║                            ✦ EMPLOYEE DETAILS ✦                             ║\n" +
               "╠══════════════════════════════════════════════════════════════════════════════╣\n" +
               "║                                                                              ║\n" +
               "║  📱  Employee ID      : " + padRight(String.valueOf(id), 55) + "║\n" +
               "║                                                                              ║\n" +
               "║  👤  Name             : " + padRight(name, 55) + "║\n" +
               "║                                                                              ║\n" +
               "║  🏢  Department       : " + padRight(department, 55) + "║\n" +
               "║                                                                              ║\n" +
               "║  💼  Position         : " + padRight(position, 55) + "║\n" +
               "║                                                                              ║\n" +
               "║  ☎️   Contact         : " + padRight(contact, 55) + "║\n" +
               "║                                                                              ║\n" +
               "║  📧  Email            : " + padRight(email, 55) + "║\n" +
               "║                                                                              ║\n" +
               "║    Address          : " + padRight(address, 55) + "║\n" +
               "║                                                                              ║\n" +
               "╚══════════════════════════════════════════════════════════════════════════════╝\n\n";
    }

    private String padRight(String str, int length) {
        if (str == null) str = "";
        if (str.length() >= length) return str.substring(0, length);
        return str + " ".repeat(length - str.length());
    }
}