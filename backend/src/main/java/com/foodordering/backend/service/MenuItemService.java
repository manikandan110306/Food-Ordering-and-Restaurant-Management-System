
package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodordering.backend.dto.MenuItemRequest;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.MenuItemRepository;
import com.foodordering.backend.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    private final RestaurantRepository restaurantRepository;

    public MenuItem addMenuItem(Long restaurantId, MenuItemRequest request, User owner) 
    {
        Restaurant restaurant = getOwnedApprovedRestaurant(restaurantId, owner);
        boolean alreadyExists = menuItemRepository.existsByNameAndRestaurantId( request.getName(), restaurantId);
        if (alreadyExists) 
        {
            throw new IllegalArgumentException( "A food item with this name already exists in this restaurant");
        }

        MenuItem menuItem = new MenuItem();

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setAvailable(true);
        menuItem.setRestaurant(restaurant);

        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId, User owner) 
    {

        getOwnedApprovedRestaurant(restaurantId, owner);

        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem updateMenuItem(Long menuItemId, MenuItemRequest request, User owner) 
    {
        MenuItem menuItem = getOwnedMenuItem(menuItemId, owner);
        
        boolean duplicateExists =
                menuItemRepository.findByRestaurantId(
                        menuItem.getRestaurant().getId())
                        .stream()
                        .anyMatch(item ->
                                !item.getId().equals(menuItemId)
                                && item.getName().equalsIgnoreCase(request.getName()));

        if (duplicateExists) {
            throw new IllegalArgumentException(
                    "Another food item with this name already exists");
        }

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setImageUrl(request.getImageUrl());

        return menuItemRepository.save(menuItem);
    }

    // Delete a food item
    public void deleteMenuItem(Long menuItemId, User owner) {

        MenuItem menuItem = getOwnedMenuItem(menuItemId, owner);

        menuItemRepository.delete(menuItem);
    }

    // Change food item availability
    public MenuItem updateAvailability(Long menuItemId,
                                       boolean available,
                                       User owner) {

        MenuItem menuItem = getOwnedMenuItem(menuItemId, owner);

        menuItem.setAvailable(available);

        return menuItemRepository.save(menuItem);
    }

    // Verify that the restaurant belongs to the logged-in owner
    // and has been approved by an admin
    private Restaurant getOwnedApprovedRestaurant(Long restaurantId, User owner) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Restaurant not found with ID: " + restaurantId));

        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException(
                    "You are not authorized to manage this restaurant");
        }

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "Restaurant must be approved before managing its menu");
        }

        return restaurant;
    }

    // Verify that the food item belongs to the logged-in owner's restaurant
    private MenuItem getOwnedMenuItem(Long menuItemId, User owner) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Menu item not found with ID: " + menuItemId));

        getOwnedApprovedRestaurant(
                menuItem.getRestaurant().getId(), owner);

        return menuItem;
    }
}
