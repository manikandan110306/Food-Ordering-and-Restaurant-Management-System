package com.foodordering.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.CartResponse;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Add a food item to the cart
    @PostMapping("/items/{menuItemId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long menuItemId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            CartResponse cartResponse =
                    cartService.addToCart(menuItemId, quantity, customer);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cartResponse);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Get the customer's cart
    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            CartResponse cartResponse = cartService.getCart(customer);

            return ResponseEntity.ok(cartResponse);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Update the quantity of a food item
    @PatchMapping("/items/{menuItemId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long menuItemId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            CartResponse cartResponse =
                    cartService.updateQuantity(menuItemId, quantity, customer);

            return ResponseEntity.ok(cartResponse);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Remove one food item from the cart
    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Long menuItemId,
            Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            CartResponse cartResponse =
                    cartService.removeItem(menuItemId, customer);

            return ResponseEntity.ok(cartResponse);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Clear the entire cart
    @DeleteMapping
    public ResponseEntity<?> clearCart(Authentication authentication) {

        try {

            User customer = (User) authentication.getPrincipal();

            CartResponse cartResponse = cartService.clearCart(customer);

            return ResponseEntity.ok(cartResponse);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}