package com.foodordering.backend.dto;

import lombok.Getter;

@Getter
public class RestaurantRatingResponse {

    private Long restaurantId;

    private String restaurantName;

    private String address;

    private Double averageRating;

    private Long totalReviews;

    public RestaurantRatingResponse(
            Long restaurantId,
            String restaurantName,
            String address,
            Double averageRating,
            Long totalReviews
    ) {

        this.restaurantId = restaurantId;

        this.restaurantName = restaurantName;

        this.address = address;

        this.averageRating = averageRating;

        this.totalReviews = totalReviews;
    }
}