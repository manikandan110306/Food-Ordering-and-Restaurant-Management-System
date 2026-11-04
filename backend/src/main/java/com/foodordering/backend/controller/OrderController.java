package com.foodordering.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.OrderRequest;
import com.foodordering.backend.dto.OrderResponse;
import com.foodordering.backend.dto.OrderStatusUpdateRequest;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Customer: Place a new order.

    @PostMapping
    public ResponseEntity<?> placeOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            OrderResponse orderResponse = orderService.placeOrder(request, customer);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(orderResponse);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Customer: View all personal orders.

    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(
            Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            List<OrderResponse> orders = orderService.getCustomerOrders(customer);

            return ResponseEntity.ok(orders);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Customer: View one particular order.

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            OrderResponse order = orderService.getCustomerOrderById(orderId, customer);

            return ResponseEntity.ok(order);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Restaurant owner: View orders from their restaurants.

    @GetMapping("/restaurant")
    public ResponseEntity<?> getRestaurantOrders(
            Authentication authentication) {

        try {

            User owner = (User) authentication.getPrincipal();

            List<OrderResponse> orders = orderService.getOwnerOrders(owner);

            return ResponseEntity.ok(orders);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request,
            Authentication authentication) {

        try {

            // Get the currently logged-in user.
            User owner = (User) authentication.getPrincipal();

            // Update the order status through the service layer.
            OrderResponse response = orderService.updateOrderStatus(
                    orderId,
                    request.getStatus(),
                    owner);

            // Return the updated order details.
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            // Return an error if the order or status is invalid.
            return ResponseEntity.badRequest().body(
                    exception.getMessage());
        }
    }
}