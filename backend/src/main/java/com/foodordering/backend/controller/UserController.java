package com.foodordering.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.RegisterRequest;
import com.foodordering.backend.dto.UserResponse;
import com.foodordering.backend.dto.LoginRequest;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.UserService;

import io.jsonwebtoken.Jwt;

import com.foodordering.backend.dto.LoginResponse;
import com.foodordering.backend.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController( UserService userService, JwtService jwtService) 
    {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterRequest request) {
        try {
            User savedUser = userService.registerCustomer(
                    request.getFullName(),
                    request.getEmail(),
                    request.getPassword());

            UserResponse response = new UserResponse(savedUser);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.login(
                    request.getEmail(),
                    request.getPassword());

            String token = jwtService.generateToken(user);

            UserResponse userResponse = new UserResponse(user);

            LoginResponse loginResponse = new LoginResponse(
                    token,
                    "Bearer",
                    userResponse);

            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", exception.getMessage()));
        }
    }

}
