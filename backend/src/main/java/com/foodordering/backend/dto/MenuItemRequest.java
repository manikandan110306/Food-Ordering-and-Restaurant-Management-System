
package com.foodordering.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemRequest {

    @NotBlank(message = "Food name is required")
    @Size(max = 150, message = "Food name cannot exceed 150 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Food price is required")
    @DecimalMin(value = "0.01", message = "Food price must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Price can have at most 8 integer digits and 2 decimal places")
    private BigDecimal price;

    @NotBlank(message = "Food category is required")
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

}
