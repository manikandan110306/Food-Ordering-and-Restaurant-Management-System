package com.foodordering.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class UpdateProfileRequest {
    
    @NotBlank (message = "Fullname is required")
    @Size (max=100,message = "Fullname cannot exceed 100 characters")
    private String fullName;
}
