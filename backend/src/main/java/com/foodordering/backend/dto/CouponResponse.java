package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.foodordering.backend.entity.Coupon;
import com.foodordering.backend.entity.DiscountType;

import lombok.Getter;

@Getter
public class CouponResponse {

    private Long id;

    private String code;

    private String description;

    private DiscountType discountType;

    private BigDecimal discountValue;

    private BigDecimal minimumOrderAmount;

    private BigDecimal maximumDiscount;

    private LocalDateTime startDate;

    private LocalDateTime expiryDate;

    private Integer usageLimit;

    private Integer usedCount;

    private boolean active;

    private LocalDateTime createdAt;

    public CouponResponse(Coupon coupon) {

        this.id = coupon.getId();

        this.code = coupon.getCode();

        this.description = coupon.getDescription();

        this.discountType = coupon.getDiscountType();

        this.discountValue = coupon.getDiscountValue();

        this.minimumOrderAmount = coupon.getMinimumOrderAmount();

        this.maximumDiscount = coupon.getMaximumDiscount();

        this.startDate = coupon.getStartDate();

        this.expiryDate = coupon.getExpiryDate();

        this.usageLimit = coupon.getUsageLimit();

        this.usedCount = coupon.getUsedCount();

        this.active = coupon.isActive();

        this.createdAt = coupon.getCreatedAt();
    }
}