package com.foodordering.backend.dto;

import java.time.LocalDateTime;

import com.foodordering.backend.entity.RestaurantReview;

import lombok.Getter;

@Getter
public class ReviewResponse {

    private Long reviewId;

    private Long orderId;

    private Long restaurantId;

    private String customerName;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    public ReviewResponse(RestaurantReview review) {

        this.reviewId = review.getId();

        this.orderId = review.getCustomerOrder().getId();

        this.restaurantId = review.getCustomerOrder()
                .getRestaurant()
                .getId();

        this.customerName = review.getCustomerOrder()
                .getCustomer()
                .getFullName();

        this.rating = review.getRating();

        this.comment = review.getComment();

        this.createdAt = review.getCreatedAt();
    }
}