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

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/auth")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService=userService;
    }

    @PostMapping ("/register")
    public ResponseEntity<?>registerCustomer( @Valid @RequestBody RegisterRequest request )
    {
        try
        {
            User savedUser=userService.registerCustomer(
                request.getFullName(), 
                request.getEmail(), 
                request.getPassword()
            );

            UserResponse response=new UserResponse(savedUser);

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        }
        catch(IllegalArgumentException exception)
        {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message",exception.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login( @Valid @RequestBody LoginRequest request )
    {
        try
        {
            User user=userService.login(
                request.getEmail(),
                request.getPassword()
            );
            UserResponse response=new UserResponse(user);

            return ResponseEntity.ok(response);
        }
        catch(IllegalArgumentException exception)
        {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message",exception.getMessage()));
        }
    }

}
