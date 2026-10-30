package com.foodordering.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long>{
    
    List<Restaurant>findByOwnerId(Long ownerId);
    List<Restaurant>findByStatus(RestaurantStatus status);
    boolean existsByNameAndOwnerId(String name,Long ownerId);
}
