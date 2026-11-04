package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.foodordering.backend.entity.CustomerOrder;

import com.foodordering.backend.entity.OrderItem;

import lombok.Getter;

@Getter
public class OrderResponse {

    private Long orderId;

    private Long restaurantId;

    private String restaurantName;

    private String orderStatus;

    private List<OrderItemResponse> items;

    private BigDecimal totalAmount;

    private String deliveryAddress;

    private String deliveryPhone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public OrderResponse(CustomerOrder order) {

        this.orderId = order.getId();

        this.restaurantId = order.getRestaurant().getId();

        this.restaurantName = order.getRestaurant().getName();

        this.orderStatus = order.getStatus().name();

        this.items = order.getItems()
                .stream()
                .map(OrderItemResponse::new)
                .toList();

        this.totalAmount = order.getTotalAmount();

        this.deliveryAddress = order.getDeliveryAddress();

        this.deliveryPhone = order.getDeliveryPhone();

        this.createdAt = order.getCreatedAt();

        this.updatedAt = order.getUpdatedAt();
    }
}