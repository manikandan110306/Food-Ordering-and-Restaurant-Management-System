package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.RestaurantReview;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {

    Optional<RestaurantReview> findByCustomerOrderId(Long orderId);

    boolean existsByCustomerOrderId(Long orderId);

    List<RestaurantReview> findByCustomerOrder_Restaurant_IdOrderByCreatedAtDesc(
            Long restaurantId
    );

}