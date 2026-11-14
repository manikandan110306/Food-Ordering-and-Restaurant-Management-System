package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.foodordering.backend.entity.FavoriteMenuItem;

import lombok.Getter;

@Getter
public class FavoriteMenuItemResponse {

    private Long favoriteId;

    private Long menuItemId;

    private String foodName;

    private String description;

    private BigDecimal price;

    private String category;

    private String imageUrl;

    private boolean available;

    private Long restaurantId;

    private String restaurantName;

    private LocalDateTime savedAt;

    public FavoriteMenuItemResponse(FavoriteMenuItem favorite) {

        this.favoriteId = favorite.getId();

        this.menuItemId = favorite.getMenuItem().getId();

        this.foodName = favorite.getMenuItem().getName();

        this.description = favorite.getMenuItem().getDescription();

        this.price = favorite.getMenuItem().getPrice();

        this.category = favorite.getMenuItem().getCategory();

        this.imageUrl = favorite.getMenuItem().getImageUrl();

        this.available = favorite.getMenuItem().isAvailable();

        this.restaurantId = favorite.getMenuItem().getRestaurant().getId();

        this.restaurantName = favorite.getMenuItem().getRestaurant().getName();

        this.savedAt = favorite.getCreatedAt();
    }
}