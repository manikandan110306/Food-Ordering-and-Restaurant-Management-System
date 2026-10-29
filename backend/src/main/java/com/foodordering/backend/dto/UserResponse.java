package com.foodordering.backend.dto;

import java.time.LocalDateTime;

import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}