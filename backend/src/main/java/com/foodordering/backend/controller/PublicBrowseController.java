package com.foodordering.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.MenuItemResponse;
import com.foodordering.backend.dto.RestaurantResponse;
import com.foodordering.backend.service.PublicBrowseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicBrowseController {

    private final PublicBrowseService publicBrowseService;

    // Get all approved restaurants
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantResponse>> getApprovedRestaurants() {

        List<RestaurantResponse> restaurants =
                publicBrowseService.getApprovedRestaurants();

        return ResponseEntity.ok(restaurants);
    }

    // Get available menu items from a particular restaurant
    @GetMapping("/restaurants/{restaurantId}/menu")
    public ResponseEntity<?> getRestaurantMenu(
            @PathVariable Long restaurantId) {

        try {

            List<MenuItemResponse> menuItems =
                    publicBrowseService.getAvailableMenuItems(restaurantId);

            return ResponseEntity.ok(menuItems);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}