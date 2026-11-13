package com.foodordering.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.RestaurantStatus;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);

    boolean existsByNameAndRestaurantId(
            String name,
            Long restaurantId
    );

    List<MenuItem>findByRestaurant_StatusAndAvailableTrueAndNameContainingIgnoreCase(
        RestaurantStatus status,
        String name
    );

    List<MenuItem>findByRestaurant_StatusAndAvailableTrueAndCategoryIgnoreCase(
        RestaurantStatus status,
        String category
    );
}