package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.ReviewRequest;
import com.foodordering.backend.dto.ReviewResponse;
import com.foodordering.backend.entity.CustomerOrder;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.entity.RestaurantReview;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.CustomerOrderRepository;
import com.foodordering.backend.repository.RestaurantReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final RestaurantReviewRepository reviewRepository;

    private final CustomerOrderRepository customerOrderRepository;

    // Submit a review for a delivered order.
    @Transactional
    public ReviewResponse submitReview(
            Long orderId,
            ReviewRequest request,
            User customer
    ) {

        // Step 1: Check whether the logged-in user is a customer.
        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can submit restaurant reviews"
            );
        }

        // Step 2: Find the order belonging to this customer.
        CustomerOrder order = customerOrderRepository
                .findByIdAndCustomerId(orderId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found or you do not have permission to review it"
                ));

        // Step 3: Check whether the order has been delivered.
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException(
                    "You can only review delivered orders"
            );
        }

        // Step 4: Check whether the order already has a review.
        if (reviewRepository.existsByCustomerOrderId(orderId)) {
            throw new IllegalArgumentException(
                    "You have already submitted a review for this order"
            );
        }

        // Step 5: Create a new review.
        RestaurantReview review = new RestaurantReview();

        review.setCustomerOrder(order);

        review.setRating(request.getRating());

        review.setComment(request.getComment());

        // Step 6: Save the review.
        RestaurantReview savedReview = reviewRepository.save(review);

        // Step 7: Return the review response.
        return new ReviewResponse(savedReview);
    }

    // Retrieve all reviews for a restaurant.
    @Transactional(readOnly = true)
    public List<ReviewResponse> getRestaurantReviews(Long restaurantId) {

        return reviewRepository
                .findByCustomerOrder_Restaurant_IdOrderByCreatedAtDesc(
                        restaurantId
                )
                .stream()
                .map(ReviewResponse::new)
                .toList();
    }
}