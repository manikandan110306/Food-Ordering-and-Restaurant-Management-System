package com.foodordering.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins="http://localhost:8081")
@RestController 
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public Map<String,String> checkHealth() 
    {
        return Map.of(
            "status","UP",
            "message","Food Ordering Backend is running"
        );
    }
}
