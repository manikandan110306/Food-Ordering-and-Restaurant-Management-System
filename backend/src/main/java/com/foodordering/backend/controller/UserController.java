package com.foodordering.backend.controller;

import java.util.Map;

import org.apache.catalina.connector.Response;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.foodordering.backend.dto.UpdateProfileRequest;
import com.foodordering.backend.dto.ChangePasswordRequest;
import org.springframework.web.bind.annotation.PutMapping;

import io.jsonwebtoken.Jwt;

import com.foodordering.backend.dto.LoginResponse;
import com.foodordering.backend.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
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

    @PostMapping("/register/owner")
    public ResponseEntity<?> registerRestaurantOwner( @Valid @RequestBody RegisterRequest request) 
    {
        try 
        {
            User savedUser = userService.registerRestaurantOwner(
                request.getFullName(),
                request.getEmail(),
                request.getPassword()
            );

            UserResponse response = new UserResponse(savedUser);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } 
        catch (IllegalArgumentException exception) 
        {
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

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserResponse response = new UserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        User updatedUser = userService.updateProfile(currentUser, request.getFullName());

        UserResponse response = new UserResponse(updatedUser);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        try {
            userService.changePassword(
                    currentUser,
                    request.getCurrentPassword(),
                    request.getNewPassword());

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Password Changed Sucessfully"));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            exception.getMessage()));
        }
    }

}
