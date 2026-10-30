package com.foodordering.backend.dto;

import java.time.LocalDateTime;

import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;

import lombok.Getter;

@Getter
public class RestaurantResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private RestaurantStatus status;
    private LocalDateTime createdAt;

    public RestaurantResponse(Restaurant restaurant) {

        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.description = restaurant.getDescription();
        this.address = restaurant.getAddress();
        this.phone = restaurant.getPhone();
        this.email = restaurant.getEmail();
        this.status = restaurant.getStatus();
        this.createdAt = restaurant.getCreatedAt();
    }
}