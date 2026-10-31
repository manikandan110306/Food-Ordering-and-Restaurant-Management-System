package com.foodordering.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.RestaurantResponse;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.RestaurantService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final RestaurantService restaurantService;

    public AdminController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants/pending")
    public ResponseEntity<?> getPendingRestaurants(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can view pending restaurants");
        }

        List<Restaurant> restaurants = restaurantService.getPendingRestaurants();

        List<RestaurantResponse> response = restaurants.stream()
                .map(RestaurantResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/restaurants/{id}/approve")
    public ResponseEntity<?> approveRestaurant(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can approve restaurants");
        }
        try {
            Restaurant restaurant = restaurantService.approveRestaurant(id);

            RestaurantResponse response = new RestaurantResponse(restaurant);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @PutMapping("/restaurants/{id}/reject")
    public ResponseEntity<?> rejectRestaurant( @PathVariable Long id, Authentication authentication) 
    {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can reject restaurants");
        }

        try {
            Restaurant restaurant = restaurantService.rejectRestaurant(id);

            RestaurantResponse response = new RestaurantResponse(restaurant);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

}
