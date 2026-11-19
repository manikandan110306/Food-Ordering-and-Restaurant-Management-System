package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.foodordering.backend.entity.DiscountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponRequest {

    @NotBlank(message = "Coupon code is required")
    @Size(max = 50, message = "Coupon code cannot exceed 50 characters")
    @Pattern(
        regexp = "^[A-Za-z0-9_-]+$",
        message = "Coupon code can contain only letters, numbers, underscores and hyphens"
    )
    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Discount type is required")
    private DiscountType discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(
        value = "0.01",
        message = "Discount value must be greater than zero"
    )
    private BigDecimal discountValue;

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(
        value = "0.00",
        message = "Minimum order amount cannot be negative"
    )
    private BigDecimal minimumOrderAmount = BigDecimal.ZERO;

    @DecimalMin(
        value = "0.01",
        message = "Maximum discount must be greater than zero"
    )
    private BigDecimal maximumDiscount;

    @NotNull(message = "Coupon start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "Coupon expiry date is required")
    private LocalDateTime expiryDate;

    @NotNull(message = "Usage limit is required")
    @Positive(message = "Usage limit must be greater than zero")
    private Integer usageLimit;

    private boolean active = true;
}