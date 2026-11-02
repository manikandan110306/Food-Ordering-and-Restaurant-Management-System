package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodordering.backend.dto.MenuItemResponse;
import com.foodordering.backend.dto.RestaurantResponse;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.repository.MenuItemRepository;
import com.foodordering.backend.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicBrowseService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    // Get all approved restaurants
    public List<RestaurantResponse> getApprovedRestaurants() {

        List<Restaurant> restaurants = restaurantRepository.findByStatus(RestaurantStatus.APPROVED);

        return restaurants.stream()
                .map(RestaurantResponse::new)
                .toList();
    }

    // Get available menu items from an approved restaurant
    public List<MenuItemResponse> getAvailableMenuItems(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Restaurant not found with ID: " + restaurantId));

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "This restaurant is not currently available");
        }

        List<MenuItem> menuItems =
                menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);

        return menuItems.stream()
                .map(MenuItemResponse::new)
                .toList();
    }
}