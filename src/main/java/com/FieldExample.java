package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FieldExample {

    @Autowired
    private EmployeeService employeeService;

    public void display() {
        System.out.println("Field Injection:");
        employeeService.showEmployee();
    }
}
