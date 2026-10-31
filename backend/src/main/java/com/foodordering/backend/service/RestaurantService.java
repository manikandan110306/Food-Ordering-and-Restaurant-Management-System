package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.RestaurantRepository;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getRestaurantsByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }

    public List<Restaurant> getPendingRestaurants() {
        return restaurantRepository.findByStatus(RestaurantStatus.PENDING);
    }

    public Restaurant registerRestaurant(
            String name,
            String description,
            String address,
            String phone,
            String email,
            User owner) {

        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new IllegalArgumentException("Only restaurant owners can register restaurants");
        }

        if (restaurantRepository.existsByNameAndOwnerId(name, owner.getId())) {

            throw new IllegalArgumentException("You have already registered a restaurant with this name");
        }

        Restaurant restaurant = new Restaurant();

        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setEmail(email);
        restaurant.setOwner(owner);
        restaurant.setStatus(RestaurantStatus.PENDING);

        return restaurantRepository.save(restaurant);
    }

    public Restaurant approveRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Restaurant not found with ID: " + restaurantId));

        if (restaurant.getStatus() != RestaurantStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending restaurants can be approved");
        }

        restaurant.setStatus(RestaurantStatus.APPROVED);

        return restaurantRepository.save(restaurant);
    }

    public Restaurant rejectRestaurant(Long restaurantId) 
    {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Restaurant not found with ID: " + restaurantId));

        if (restaurant.getStatus() != RestaurantStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending restaurants can be rejected");
        }

        restaurant.setStatus(RestaurantStatus.REJECTED);

        return restaurantRepository.save(restaurant);
    }
}
