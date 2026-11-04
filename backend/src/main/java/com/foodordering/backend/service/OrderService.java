package com.foodordering.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.OrderRequest;
import com.foodordering.backend.dto.OrderResponse;
import com.foodordering.backend.entity.Cart;
import com.foodordering.backend.entity.CartItem;
import com.foodordering.backend.entity.CustomerOrder;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.OrderItem;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.CartRepository;
import com.foodordering.backend.repository.CustomerOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;

    private final CustomerOrderRepository customerOrderRepository;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request, User customer) {

        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can place orders");
        }

        Cart cart = cartRepository.findByUserId(customer.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cart not found"));

        List<CartItem> cartItems = cart.getItems();

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot place an order with an empty cart");
        }

        Restaurant restaurant = cartItems.get(0)
                .getMenuItem()
                .getRestaurant();

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "Restaurant is not approved");
        }

        CustomerOrder order = new CustomerOrder();

        order.setCustomer(customer);

        order.setRestaurant(restaurant);

        order.setStatus(OrderStatus.PENDING);

        order.setDeliveryAddress(request.getDeliveryAddress().trim());

        order.setDeliveryPhone(request.getDeliveryPhone().trim());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            MenuItem menuItem = cartItem.getMenuItem();

            if (!menuItem.isAvailable()) {
                throw new IllegalArgumentException(
                        menuItem.getName() + " is currently unavailable");
            }

            if (menuItem.getRestaurant().getStatus() != RestaurantStatus.APPROVED) {
                throw new IllegalArgumentException(
                        "Restaurant is not approved");
            }

            if (!menuItem.getRestaurant().getId()
                    .equals(restaurant.getId())) {
                throw new IllegalArgumentException(
                        "All cart items must belong to the same restaurant");
            }

            BigDecimal unitPrice = menuItem.getPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = new OrderItem();

            orderItem.setCustomerOrder(order);

            orderItem.setMenuItem(menuItem);

            orderItem.setFoodName(menuItem.getName());

            orderItem.setUnitPrice(unitPrice);

            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setSubtotal(subtotal);

            order.getItems().add(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        cart.getItems().clear();

        return new OrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getCustomerOrders(User customer) {

        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can view their orders");
        }

        return customerOrderRepository
                .findByCustomerIdOrderByCreatedAtDesc(customer.getId())
                .stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getCustomerOrderById(
            Long orderId,
            User customer) {

        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can view their orders");
        }

        CustomerOrder order = customerOrderRepository
                .findByIdAndCustomerId(orderId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found"));

        return new OrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOwnerOrders(User owner) {

        // Step 1: Verify the user's role.

        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new IllegalArgumentException(
                    "Only restaurant owners can view restaurant orders");
        }

        // Step 2: Retrieve orders belonging to this owner's restaurants.

        return customerOrderRepository
                .findByRestaurant_Owner_IdOrderByCreatedAtDesc(owner.getId())
                .stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus newStatus,
            User owner) {

        // Step 1: Check whether the logged-in user is a restaurant owner.
        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new IllegalArgumentException(
                    "Only restaurant owners can update order status");
        }

        // Step 2: Check whether a new status was provided.
        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "New order status is required");
        }

        // Step 3: Find the order belonging to this restaurant owner.
        CustomerOrder order = customerOrderRepository
                .findByIdAndRestaurant_Owner_Id(orderId, owner.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found or you do not have permission to update it"));

        // Step 4: Get the current order status.
        OrderStatus currentStatus = order.getStatus();

        // Step 5: Validate the status transition.
        boolean validTransition = switch (currentStatus) {

            case PENDING ->
                newStatus == OrderStatus.CONFIRMED;

            case CONFIRMED ->
                newStatus == OrderStatus.PREPARING;

            case PREPARING ->
                newStatus == OrderStatus.READY;

            case READY ->
                newStatus == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY ->
                newStatus == OrderStatus.DELIVERED;

            default -> false;
        };

        // Step 6: Reject invalid status transitions.
        if (!validTransition) {
            throw new IllegalArgumentException(
                    "Invalid order status transition from "
                            + currentStatus + " to " + newStatus);
        }

        // Step 7: Update and save the order status.
        order.setStatus(newStatus);

        CustomerOrder updatedOrder = customerOrderRepository.save(order);

        // Step 8: Return the updated order details.
        return new OrderResponse(updatedOrder);
    }
}