package com.foodordering.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminDashboardResponse {

    private long totalUsers;

    private long totalCustomers;

    private long totalRestaurantOwners;

    private long totalRestaurants;

    private long pendingRestaurants;

    private long approvedRestaurants;

    private long rejectedRestaurants;

    private long totalOrders;

    private long pendingOrders;

    private long deliveredOrders;

    private long cancelledOrders;

    private BigDecimal totalRevenue;
}