package com.foodordering.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodordering.backend.dto.RestaurantRatingResponse;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByOwnerId(Long ownerId);

    List<Restaurant> findByStatus(RestaurantStatus status);

    boolean existsByNameAndOwnerId(String name, Long ownerId);

    List<Restaurant> findByStatusAndNameContainingIgnoreCase(
            RestaurantStatus status,
            String name
    );

    List<Restaurant> findByStatusAndAddressContainingIgnoreCase(
            RestaurantStatus status,
            String address
    );

    @Query("""
            SELECT new com.foodordering.backend.dto.RestaurantRatingResponse(
                r.id,
                r.name,
                r.address,
                AVG(review.rating),
                COUNT(review.id)
            )
            FROM Restaurant r
            LEFT JOIN RestaurantReview review
                ON review.customerOrder.restaurant = r
            WHERE r.status = :status
            GROUP BY r.id, r.name, r.address
            ORDER BY AVG(review.rating) DESC
            """)
    List<RestaurantRatingResponse> findApprovedRestaurantsByRating(
            @Param("status") RestaurantStatus status
    );
}