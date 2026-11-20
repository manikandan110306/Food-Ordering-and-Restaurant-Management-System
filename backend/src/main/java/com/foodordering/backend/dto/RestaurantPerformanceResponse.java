package com.foodordering.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantPerformanceResponse {

    private Long restaurantId;

    private String restaurantName;

    private String address;

    private long totalOrders;

    private long deliveredOrders;

    private BigDecimal totalRevenue;
}