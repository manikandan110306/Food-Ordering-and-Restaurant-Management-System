package com.foodordering.backend.dto;

import java.time.LocalDateTime;

import com.foodordering.backend.entity.FavoriteRestaurant;

import lombok.Getter;

@Getter
public class FavoriteRestaurantResponse {

    private Long favoriteId;

    private Long restaurantId;

    private String restaurantName;

    private String description;

    private String address;

    private String phone;

    private LocalDateTime savedAt;

    public FavoriteRestaurantResponse(FavoriteRestaurant favorite) {

        this.favoriteId = favorite.getId();

        this.restaurantId = favorite.getRestaurant().getId();

        this.restaurantName = favorite.getRestaurant().getName();

        this.description = favorite.getRestaurant().getDescription();

        this.address = favorite.getRestaurant().getAddress();

        this.phone = favorite.getRestaurant().getPhone();

        this.savedAt = favorite.getCreatedAt();
    }
}