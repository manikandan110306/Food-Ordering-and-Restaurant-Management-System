
package com.foodordering.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.OrderRequest;
import com.foodordering.backend.dto.OrderResponse;

import com.foodordering.backend.entity.Cart;
import com.foodordering.backend.entity.CartItem;
import com.foodordering.backend.entity.CustomerAddress;
import com.foodordering.backend.entity.CustomerOrder;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.OrderItem;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.entity.Payment;
import com.foodordering.backend.entity.PaymentMethod;
import com.foodordering.backend.entity.PaymentStatus;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;

import com.foodordering.backend.repository.CartRepository;
import com.foodordering.backend.repository.CustomerAddressRepository;
import com.foodordering.backend.repository.CustomerOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;

    private final CustomerOrderRepository customerOrderRepository;

    private final CustomerAddressRepository addressRepository;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request, User customer) {

        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can place orders");
        }

        // Step 1: Retrieve the selected saved address.

        CustomerAddress selectedAddress = addressRepository
                .findByIdAndUserId(request.getAddressId(), customer.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Delivery address not found or does not belong to you"));

        // Step 2: Retrieve the customer's cart.

        Cart cart = cartRepository.findByUserId(customer.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cart not found"));

        List<CartItem> cartItems = cart.getItems();

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot place an order with an empty cart");
        }

        // Step 3: Identify the restaurant.

        Restaurant restaurant = cartItems.get(0)
                .getMenuItem()
                .getRestaurant();

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "Restaurant is not approved");
        }

        // Step 4: Create the order.

        CustomerOrder order = new CustomerOrder();

        order.setCustomer(customer);

        order.setRestaurant(restaurant);

        order.setStatus(OrderStatus.PENDING);

        // Step 5: Save a snapshot of the selected delivery address.

        String deliveryAddress = buildDeliveryAddress(selectedAddress);

        if (deliveryAddress.length() > 500) {
            throw new IllegalArgumentException(
                    "Delivery address is too long");
        }

        order.setDeliveryAddress(deliveryAddress);

        order.setDeliveryPhone(selectedAddress.getPhone());

        // Step 6: Calculate the order total and create order items.

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            MenuItem menuItem = cartItem.getMenuItem();

            if (!menuItem.isAvailable()) {
                throw new IllegalArgumentException(
                        menuItem.getName() + " is currently unavailable");
            }

            if (menuItem.getRestaurant().getStatus()
                    != RestaurantStatus.APPROVED) {
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

        // Step 7: Create the Cash on Delivery payment.

        Payment payment = new Payment();

        payment.setCustomerOrder(order);

        payment.setAmount(order.getTotalAmount());

        payment.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);

        payment.setPaymentStatus(PaymentStatus.PENDING);

        order.setPayment(payment);

        // Step 8: Save the order.

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        // Step 9: Clear the customer's cart.

        cart.getItems().clear();

        return new OrderResponse(savedOrder);
    }

    // Build a permanent address snapshot for the order.
    private String buildDeliveryAddress(CustomerAddress address) {

        StringBuilder result = new StringBuilder();

        appendAddressPart(result, address.getFullName());

        appendAddressPart(result, address.getAddressLine1());

        appendAddressPart(result, address.getAddressLine2());

        appendAddressPart(result, address.getLandmark());

        appendAddressPart(result, address.getCity());

        appendAddressPart(result, address.getState());

        appendAddressPart(result, address.getPostalCode());

        return result.toString();
    }

    // Add an address component with a comma separator.
    private void appendAddressPart(StringBuilder result, String value) {

        if (value != null && !value.isBlank()) {

            if (!result.isEmpty()) {
                result.append(", ");
            }

            result.append(value.trim());
        }
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

        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new IllegalArgumentException(
                    "Only restaurant owners can view restaurant orders");
        }

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

        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new IllegalArgumentException(
                    "Only restaurant owners can update order status");
        }

        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "New order status is required");
        }

        CustomerOrder order = customerOrderRepository
                .findByIdAndRestaurant_Owner_Id(orderId, owner.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found or you do not have permission to update it"));

        OrderStatus currentStatus = order.getStatus();

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

        if (!validTransition) {
            throw new IllegalArgumentException(
                    "Invalid order status transition from "
                            + currentStatus + " to " + newStatus);
        }

        order.setStatus(newStatus);

        CustomerOrder updatedOrder = customerOrderRepository.save(order);

        return new OrderResponse(updatedOrder);
    }
}
