package com.foodordering.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.FavoriteMenuItemResponse;
import com.foodordering.backend.dto.FavoriteRestaurantResponse;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // Add a restaurant to customer favourites.
    @PostMapping("/restaurants/{restaurantId}")
    public ResponseEntity<?> addFavoriteRestaurant(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal User customer
    ) {

        try {

            FavoriteRestaurantResponse response =
                    favoriteService.addFavoriteRestaurant(
                            restaurantId,
                            customer
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Get all favourite restaurants.
    @GetMapping("/restaurants")
    public ResponseEntity<?> getFavoriteRestaurants(
            @AuthenticationPrincipal User customer
    ) {

        try {

            List<FavoriteRestaurantResponse> favorites =
                    favoriteService.getFavoriteRestaurants(customer);

            return ResponseEntity.ok(favorites);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Remove a restaurant from customer favourites.
    @DeleteMapping("/restaurants/{restaurantId}")
    public ResponseEntity<?> removeFavoriteRestaurant(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal User customer
    ) {

        try {

            favoriteService.removeFavoriteRestaurant(
                    restaurantId,
                    customer
            );

            return ResponseEntity.ok(
                    "Restaurant removed from favourites successfully"
            );

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Add a food item to customer favourites.
    @PostMapping("/menu-items/{menuItemId}")
    public ResponseEntity<?> addFavoriteMenuItem(
            @PathVariable Long menuItemId,
            @AuthenticationPrincipal User customer
    ) {

        try {

            FavoriteMenuItemResponse response =
                    favoriteService.addFavoriteMenuItem(
                            menuItemId,
                            customer
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Get all favourite food items.
    @GetMapping("/menu-items")
    public ResponseEntity<?> getFavoriteMenuItems(
            @AuthenticationPrincipal User customer
    ) {

        try {

            List<FavoriteMenuItemResponse> favorites =
                    favoriteService.getFavoriteMenuItems(customer);

            return ResponseEntity.ok(favorites);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Remove a food item from customer favourites.
    @DeleteMapping("/menu-items/{menuItemId}")
    public ResponseEntity<?> removeFavoriteMenuItem(
            @PathVariable Long menuItemId,
            @AuthenticationPrincipal User customer
    ) {

        try {

            favoriteService.removeFavoriteMenuItem(
                    menuItemId,
                    customer
            );

            return ResponseEntity.ok(
                    "Food item removed from favourites successfully"
            );

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }
}