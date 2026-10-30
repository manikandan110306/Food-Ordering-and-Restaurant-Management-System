package com.foodordering.backend.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.RestaurantRequest;
import com.foodordering.backend.dto.RestaurantResponse;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.service.RestaurantService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<?> registerRestaurant( @Valid @RequestBody RestaurantRequest request, Authentication authentication) 
    {
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole() != Role.RESTAURANT_OWNER) 
        {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                    "message",
                    "Only restaurant owners can register restaurants")
            );
        }

        try 
        {
            Restaurant restaurant = restaurantService.registerRestaurant(
                    request.getName(),
                    request.getDescription(),
                    request.getAddress(),
                    request.getPhone(),
                    request.getEmail(),
                    currentUser
            );
            RestaurantResponse response = new RestaurantResponse(restaurant);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "message",
                            exception.getMessage()));
        }
    }

    

}
