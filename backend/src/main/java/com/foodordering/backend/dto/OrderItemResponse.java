package com.foodordering.backend.dto;

import java.math.BigDecimal;

import com.foodordering.backend.entity.OrderItem;

import lombok.Getter;

@Getter
public class OrderItemResponse {

    private Long id;

    private Long menuItemId;

    private String foodName;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal subtotal;

    public OrderItemResponse(OrderItem orderItem) {

        this.id = orderItem.getId();

        this.menuItemId = orderItem.getMenuItem() != null
                ? orderItem.getMenuItem().getId()
                : null;

        this.foodName = orderItem.getFoodName();

        this.unitPrice = orderItem.getUnitPrice();

        this.quantity = orderItem.getQuantity();

        this.subtotal = orderItem.getSubtotal();
    }
}