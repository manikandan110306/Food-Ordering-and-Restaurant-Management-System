package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.FavoriteMenuItem;

public interface FavoriteMenuItemRepository
        extends JpaRepository<FavoriteMenuItem, Long> {

    // Get all favourite food items belonging to a customer.
    List<FavoriteMenuItem> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    // Find a particular favourite food item belonging to a customer.
    Optional<FavoriteMenuItem> findByUserIdAndMenuItemId(
            Long userId,
            Long menuItemId
    );

    // Check whether a food item is already in the customer's favourites.
    boolean existsByUserIdAndMenuItemId(
            Long userId,
            Long menuItemId
    );
}