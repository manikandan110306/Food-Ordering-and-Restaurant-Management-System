package com.foodordering.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopSellingFoodResponse {

    private String foodName;

    private Long totalQuantitySold;

    private BigDecimal totalRevenue;
}