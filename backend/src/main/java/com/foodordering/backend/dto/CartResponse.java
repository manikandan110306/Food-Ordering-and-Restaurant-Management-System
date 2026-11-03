package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.foodordering.backend.entity.Cart;

import lombok.Getter;

@Getter
public class CartResponse {

    private Long cartId;

    private List<CartItemResponse> items;

    private Integer totalItems;

    private BigDecimal totalAmount;

    public CartResponse(Cart cart) {

        this.cartId = cart.getId();

        this.items = cart.getItems()
                .stream()
                .map(CartItemResponse::new)
                .toList();

        this.totalItems = this.items.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        this.totalAmount = this.items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}