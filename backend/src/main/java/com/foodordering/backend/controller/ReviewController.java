package com.foodordering.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.ReviewRequest;
import com.foodordering.backend.dto.ReviewResponse;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Submit a review for a delivered order.
    @PostMapping("/orders/{orderId}")
    public ResponseEntity<?> submitReview(
            @PathVariable Long orderId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal User customer
    ) {

        try {

            ReviewResponse response = reviewService.submitReview(
                    orderId,
                    request,
                    customer
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // View all reviews for a restaurant.
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReviewResponse>> getRestaurantReviews(
            @PathVariable Long restaurantId
    ) {

        List<ReviewResponse> reviews =
                reviewService.getRestaurantReviews(restaurantId);

        return ResponseEntity.ok(reviews);
    }
}