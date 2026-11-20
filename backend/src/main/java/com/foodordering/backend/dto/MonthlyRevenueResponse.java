package com.foodordering.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyRevenueResponse {

    private int year;

    private int month;

    private BigDecimal totalRevenue;
}