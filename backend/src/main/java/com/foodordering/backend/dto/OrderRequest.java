
package com.foodordering.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "Delivery address ID is required")
    @Positive(message = "Delivery address ID must be greater than zero")
    private Long addressId;

    @Size(max = 50, message = "Coupon code cannot exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]+$",
            message = "Coupon code contains invalid characters"
    )
    private String couponCode;
}
