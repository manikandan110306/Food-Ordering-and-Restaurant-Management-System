
package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.foodordering.backend.entity.MenuItem;

import lombok.Getter;

@Getter
public class MenuItemResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String category;

    private String imageUrl;

    private boolean available;

    private Long restaurantId;

    private LocalDateTime createdAt;

    public MenuItemResponse(MenuItem menuItem) {

        this.id = menuItem.getId();

        this.name = menuItem.getName();

        this.description = menuItem.getDescription();

        this.price = menuItem.getPrice();

        this.category = menuItem.getCategory();

        this.imageUrl = menuItem.getImageUrl();

        this.available = menuItem.isAvailable();

        this.restaurantId = menuItem.getRestaurant().getId();

        this.createdAt = menuItem.getCreatedAt();
    }
}
