package com.foodordering.backend.dto;

import java.math.BigDecimal;

import com.foodordering.backend.entity.CartItem;

import lombok.Getter;

@Getter
public class CartItemResponse {

    private Long id;

    private Long menuItemId;

    private String foodName;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subtotal;

    private String imageUrl;

    private Long restaurantId;

    private String restaurantName;

    public CartItemResponse(CartItem cartItem) {

        this.id = cartItem.getId();

        this.menuItemId = cartItem.getMenuItem().getId();

        this.foodName = cartItem.getMenuItem().getName();

        this.description = cartItem.getMenuItem().getDescription();

        this.price = cartItem.getMenuItem().getPrice();

        this.quantity = cartItem.getQuantity();

        this.subtotal = this.price.multiply(
                BigDecimal.valueOf(this.quantity)
        );

        this.imageUrl = cartItem.getMenuItem().getImageUrl();

        this.restaurantId = cartItem.getMenuItem()
                .getRestaurant().getId();

        this.restaurantName = cartItem.getMenuItem()
                .getRestaurant().getName();
    }
}