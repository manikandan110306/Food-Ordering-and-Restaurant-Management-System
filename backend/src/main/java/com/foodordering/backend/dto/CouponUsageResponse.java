package com.foodordering.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponUsageResponse {

    private String couponCode;

    private Long totalRedemptions;

    private BigDecimal totalDiscountGiven;
}