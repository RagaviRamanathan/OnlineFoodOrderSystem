package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConstructorExample {

    private final EmployeeService employeeService;

    @Autowired
    public ConstructorExample(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void display() {
        System.out.println("Constructor Injection:");
        employeeService.showEmployee();
    }
}