package com.foodordering.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-protected")
public class TestController {

    @GetMapping
    public Map<String, String> testProtectedEndpoint() {

        return Map.of(
                "status", "SUCCESS",
                "message", "JWT authentication is working!"
        );
    }
}