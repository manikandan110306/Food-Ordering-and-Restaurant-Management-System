package com.foodordering.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.MenuItemResponse;
import com.foodordering.backend.dto.RestaurantRatingResponse;
import com.foodordering.backend.dto.RestaurantResponse;
import com.foodordering.backend.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // Search restaurants by name.
    @GetMapping("/restaurants")
    public ResponseEntity<?> searchRestaurants(
            @RequestParam String keyword
    ) {

        try {

            List<RestaurantResponse> restaurants =
                    searchService.searchRestaurants(keyword);

            return ResponseEntity.ok(restaurants);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Search food items by name.
    @GetMapping("/food")
    public ResponseEntity<?> searchFood(
            @RequestParam String keyword
    ) {

        try {

            List<MenuItemResponse> menuItems =
                    searchService.searchFood(keyword);

            return ResponseEntity.ok(menuItems);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Filter food items by category.
    @GetMapping("/category")
    public ResponseEntity<?> filterFoodByCategory(
            @RequestParam String category
    ) {

        try {

            List<MenuItemResponse> menuItems =
                    searchService.filterFoodByCategory(category);

            return ResponseEntity.ok(menuItems);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Search restaurants by location.
    @GetMapping("/location")
    public ResponseEntity<?> searchRestaurantsByLocation(
            @RequestParam String address
    ) {

        try {

            List<RestaurantResponse> restaurants =
                    searchService.searchRestaurantsByLocation(address);

            return ResponseEntity.ok(restaurants);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Get approved restaurants sorted by average rating.
    @GetMapping("/top-rated")
    public ResponseEntity<List<RestaurantRatingResponse>>
            getRestaurantsByRating() {

        List<RestaurantRatingResponse> restaurants =
                searchService.getRestaurantsByRating();

        return ResponseEntity.ok(restaurants);
    }
}