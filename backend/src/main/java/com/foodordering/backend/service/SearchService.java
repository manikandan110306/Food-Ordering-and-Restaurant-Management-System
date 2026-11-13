package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.MenuItemResponse;
import com.foodordering.backend.dto.RestaurantRatingResponse;
import com.foodordering.backend.dto.RestaurantResponse;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.repository.MenuItemRepository;
import com.foodordering.backend.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RestaurantRepository restaurantRepository;

    private final MenuItemRepository menuItemRepository;

    // Search approved restaurants by name.
    @Transactional(readOnly = true)
    public List<RestaurantResponse> searchRestaurants(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException(
                    "Restaurant search keyword cannot be empty"
            );
        }

        List<Restaurant> restaurants =
                restaurantRepository.findByStatusAndNameContainingIgnoreCase(
                        RestaurantStatus.APPROVED,
                        keyword.trim()
                );

        return restaurants.stream()
                .map(RestaurantResponse::new)
                .toList();
    }

    // Search available food items by name.
    @Transactional(readOnly = true)
    public List<MenuItemResponse> searchFood(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException(
                    "Food search keyword cannot be empty"
            );
        }

        List<MenuItem> menuItems =
                menuItemRepository
                        .findByRestaurant_StatusAndAvailableTrueAndNameContainingIgnoreCase(
                                RestaurantStatus.APPROVED,
                                keyword.trim()
                        );

        return menuItems.stream()
                .map(MenuItemResponse::new)
                .toList();
    }

    // Filter available food items by category.
    @Transactional(readOnly = true)
    public List<MenuItemResponse> filterFoodByCategory(String category) {

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Food category cannot be empty"
            );
        }

        List<MenuItem> menuItems =
                menuItemRepository
                        .findByRestaurant_StatusAndAvailableTrueAndCategoryIgnoreCase(
                                RestaurantStatus.APPROVED,
                                category.trim()
                        );

        return menuItems.stream()
                .map(MenuItemResponse::new)
                .toList();
    }

    // Search approved restaurants by location.
    @Transactional(readOnly = true)
    public List<RestaurantResponse> searchRestaurantsByLocation(
            String address
    ) {

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException(
                    "Location cannot be empty"
            );
        }

        List<Restaurant> restaurants =
                restaurantRepository
                        .findByStatusAndAddressContainingIgnoreCase(
                                RestaurantStatus.APPROVED,
                                address.trim()
                        );

        return restaurants.stream()
                .map(RestaurantResponse::new)
                .toList();
    }

    // Get approved restaurants sorted by average customer rating.
    @Transactional(readOnly = true)
    public List<RestaurantRatingResponse> getRestaurantsByRating() {

        return restaurantRepository.findApprovedRestaurantsByRating(
                RestaurantStatus.APPROVED
        );
    }
}