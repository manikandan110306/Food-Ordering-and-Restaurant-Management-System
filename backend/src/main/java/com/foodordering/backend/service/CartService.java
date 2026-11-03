package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.CartResponse;
import com.foodordering.backend.entity.Cart;
import com.foodordering.backend.entity.CartItem;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.CartItemRepository;
import com.foodordering.backend.repository.CartRepository;
import com.foodordering.backend.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;

    // Add a food item to the customer's cart
    @Transactional
    public CartResponse addToCart(
            Long menuItemId,
            Integer quantity,
            User customer) {

        validateCustomer(customer);

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Food item not found with ID: " + menuItemId));

        if (menuItem.getRestaurant().getStatus()
                != RestaurantStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "This restaurant is not available");
        }

        if (!menuItem.isAvailable()) {
            throw new IllegalArgumentException(
                    "This food item is currently unavailable");
        }

        Cart cart = getOrCreateCart(customer);

        // A cart can contain items from only one restaurant
        if (!cart.getItems().isEmpty()) {

            Long existingRestaurantId = cart.getItems().get(0)
                    .getMenuItem().getRestaurant().getId();

            Long newRestaurantId = menuItem.getRestaurant().getId();

            if (!existingRestaurantId.equals(newRestaurantId)) {
                throw new IllegalArgumentException(
                        "You can add food items from only one restaurant at a time. "
                        + "Please clear your cart before choosing another restaurant.");
            }
        }

        CartItem cartItem = cartItemRepository
                .findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(cartItem.getQuantity() + quantity);

            cartItemRepository.save(cartItem);

        } else {

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(quantity);

            cartItemRepository.save(cartItem);
        }

        return new CartResponse(cartRepository.save(cart));
    }

    // Get the customer's complete cart
    @Transactional
    public CartResponse getCart(User customer) {

        validateCustomer(customer);

        Cart cart = getOrCreateCart(customer);

        return new CartResponse(cart);
    }

    // Update the quantity of an existing food item
    @Transactional
    public CartResponse updateQuantity(
            Long menuItemId,
            Integer quantity,
            User customer) {

        validateCustomer(customer);

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        Cart cart = getOrCreateCart(customer);

        CartItem cartItem = cartItemRepository
                .findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Food item not found in your cart"));

        if (!cartItem.getMenuItem().isAvailable()) {
            throw new IllegalArgumentException(
                    "This food item is currently unavailable");
        }

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return new CartResponse(cart);
    }

    // Remove one food item from the customer's cart
    @Transactional
    public CartResponse removeItem(
            Long menuItemId,
            User customer) {

        validateCustomer(customer);

        Cart cart = getOrCreateCart(customer);

        CartItem cartItem = cartItemRepository
                .findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Food item not found in your cart"));

        cart.getItems().remove(cartItem);

        return new CartResponse(cart);
    }

    // Remove all food items from the customer's cart
    @Transactional
    public CartResponse clearCart(User customer) {

        validateCustomer(customer);

        Cart cart = getOrCreateCart(customer);

        cart.getItems().clear();

        return new CartResponse(cart);
    }

    // Find an existing cart or create a new one
    private Cart getOrCreateCart(User customer) {

        return cartRepository.findByUserId(customer.getId())
                .orElseGet(() -> {

                    Cart newCart = new Cart();

                    newCart.setUser(customer);

                    return cartRepository.save(newCart);
                });
    }

    // Ensure that only customers can manage shopping carts
    private void validateCustomer(User customer) {

        if (customer == null || customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can manage shopping carts");
        }
    }
}