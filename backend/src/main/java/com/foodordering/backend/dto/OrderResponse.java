
package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.foodordering.backend.entity.CustomerOrder;

import lombok.Getter;

@Getter
public class OrderResponse {

    private Long orderId;

    private Long restaurantId;

    private String restaurantName;

    private String orderStatus;

    private List<OrderItemResponse> items;

    private BigDecimal subtotalAmount;

    private BigDecimal discountAmount;

    private String couponCode;

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

        this.subtotalAmount = order.getSubtotalAmount();

        this.discountAmount = order.getDiscountAmount();

        this.couponCode = order.getCouponCode();

        this.totalAmount = order.getTotalAmount();

        this.deliveryAddress = order.getDeliveryAddress();

        this.deliveryPhone = order.getDeliveryPhone();

        this.createdAt = order.getCreatedAt();

        this.updatedAt = order.getUpdatedAt();
    }
}
