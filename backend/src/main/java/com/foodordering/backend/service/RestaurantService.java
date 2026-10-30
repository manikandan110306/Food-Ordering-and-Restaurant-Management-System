package com.foodordering.backend.service;

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

    public Restaurant registerRestaurant(
            String name,
            String description,
            String address,
            String phone,
            String email,
            User owner) 
    {

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
}
