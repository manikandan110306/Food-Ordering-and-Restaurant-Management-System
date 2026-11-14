package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.FavoriteMenuItemResponse;
import com.foodordering.backend.dto.FavoriteRestaurantResponse;
import com.foodordering.backend.entity.FavoriteMenuItem;
import com.foodordering.backend.entity.FavoriteRestaurant;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.FavoriteMenuItemRepository;
import com.foodordering.backend.repository.FavoriteRestaurantRepository;
import com.foodordering.backend.repository.MenuItemRepository;
import com.foodordering.backend.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    private final FavoriteMenuItemRepository favoriteMenuItemRepository;

    private final RestaurantRepository restaurantRepository;

    private final MenuItemRepository menuItemRepository;

    // Add a restaurant to customer favourites.
    @Transactional
    public FavoriteRestaurantResponse addFavoriteRestaurant(
            Long restaurantId,
            User customer
    ) {

        validateCustomer(customer);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Restaurant not found"
                ));

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "Only approved restaurants can be added to favourites"
            );
        }

        if (favoriteRestaurantRepository.existsByUserIdAndRestaurantId(
                customer.getId(), restaurantId
        )) {
            throw new IllegalArgumentException(
                    "Restaurant is already in your favourites"
            );
        }

        FavoriteRestaurant favorite = new FavoriteRestaurant();

        favorite.setUser(customer);
        favorite.setRestaurant(restaurant);

        FavoriteRestaurant savedFavorite =
                favoriteRestaurantRepository.save(favorite);

        return new FavoriteRestaurantResponse(savedFavorite);
    }

    // Get all favourite restaurants of a customer.
    @Transactional(readOnly = true)
    public List<FavoriteRestaurantResponse> getFavoriteRestaurants(
            User customer
    ) {

        validateCustomer(customer);

        return favoriteRestaurantRepository
                .findByUserIdOrderByCreatedAtDesc(customer.getId())
                .stream()
                .map(FavoriteRestaurantResponse::new)
                .toList();
    }

    // Remove a restaurant from customer favourites.
    @Transactional
    public void removeFavoriteRestaurant(
            Long restaurantId,
            User customer
    ) {

        validateCustomer(customer);

        FavoriteRestaurant favorite =
                favoriteRestaurantRepository
                        .findByUserIdAndRestaurantId(
                                customer.getId(),
                                restaurantId
                        )
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Favourite restaurant not found"
                        ));

        favoriteRestaurantRepository.delete(favorite);
    }

    // Add a food item to customer favourites.
    @Transactional
    public FavoriteMenuItemResponse addFavoriteMenuItem(
            Long menuItemId,
            User customer
    ) {

        validateCustomer(customer);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Food item not found"
                ));

        if (menuItem.getRestaurant().getStatus()
                != RestaurantStatus.APPROVED) {

            throw new IllegalArgumentException(
                    "Food from an unapproved restaurant cannot be added to favourites"
            );
        }

        if (favoriteMenuItemRepository.existsByUserIdAndMenuItemId(
                customer.getId(), menuItemId
        )) {
            throw new IllegalArgumentException(
                    "Food item is already in your favourites"
            );
        }

        FavoriteMenuItem favorite = new FavoriteMenuItem();

        favorite.setUser(customer);
        favorite.setMenuItem(menuItem);

        FavoriteMenuItem savedFavorite =
                favoriteMenuItemRepository.save(favorite);

        return new FavoriteMenuItemResponse(savedFavorite);
    }

    // Get all favourite food items of a customer.
    @Transactional(readOnly = true)
    public List<FavoriteMenuItemResponse> getFavoriteMenuItems(
            User customer
    ) {

        validateCustomer(customer);

        return favoriteMenuItemRepository
                .findByUserIdOrderByCreatedAtDesc(customer.getId())
                .stream()
                .map(FavoriteMenuItemResponse::new)
                .toList();
    }

    // Remove a food item from customer favourites.
    @Transactional
    public void removeFavoriteMenuItem(
            Long menuItemId,
            User customer
    ) {

        validateCustomer(customer);

        FavoriteMenuItem favorite =
                favoriteMenuItemRepository
                        .findByUserIdAndMenuItemId(
                                customer.getId(),
                                menuItemId
                        )
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Favourite food item not found"
                        ));

        favoriteMenuItemRepository.delete(favorite);
    }

    // Check whether the logged-in user is a customer.
    private void validateCustomer(User customer) {

        if (customer == null || customer.getRole() != Role.CUSTOMER) {

            throw new IllegalArgumentException(
                    "Only customers can manage favourites"
            );
        }
    }
}