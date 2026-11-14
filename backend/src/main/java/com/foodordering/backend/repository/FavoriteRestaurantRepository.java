package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.FavoriteRestaurant;

public interface FavoriteRestaurantRepository
        extends JpaRepository<FavoriteRestaurant, Long> {

    // Get all favourite restaurants belonging to a customer.
    List<FavoriteRestaurant> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    // Find a particular favourite restaurant belonging to a customer.
    Optional<FavoriteRestaurant> findByUserIdAndRestaurantId(
            Long userId,
            Long restaurantId
    );

    // Check whether a restaurant is already in the customer's favourites.
    boolean existsByUserIdAndRestaurantId(
            Long userId,
            Long restaurantId
    );
}