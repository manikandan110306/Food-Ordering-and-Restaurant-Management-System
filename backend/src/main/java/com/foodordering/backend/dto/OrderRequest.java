package com.foodordering.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
}