package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpELCollectionExample {

    @Value("#{{'Java', 'Spring Boot', 'MySQL'}}")
    private List<String> technologies;

    @Value("#{{'Ravi': 30000, 'Priya': 40000, 'Kumar': 35000}}")
    private Map<String, Integer> salaries;

    public void display() {

        System.out.println("Technologies:");
        System.out.println(technologies);

        System.out.println("Employee Salaries:");
        System.out.println(salaries);
    }
}