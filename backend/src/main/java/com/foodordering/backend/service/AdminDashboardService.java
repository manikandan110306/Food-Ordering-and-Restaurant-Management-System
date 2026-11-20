package com.foodordering.backend.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.AdminDashboardResponse;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.entity.PaymentStatus;
import com.foodordering.backend.entity.RestaurantStatus;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.repository.CustomerOrderRepository;
import com.foodordering.backend.repository.PaymentRepository;
import com.foodordering.backend.repository.RestaurantRepository;
import com.foodordering.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    private final CustomerOrderRepository customerOrderRepository;

    private final PaymentRepository paymentRepository;

    public AdminDashboardResponse getDashboardStatistics() {

        long totalUsers = userRepository.count();

        long totalCustomers =
                userRepository.countByRole(Role.CUSTOMER);

        long totalRestaurantOwners =
                userRepository.countByRole(Role.RESTAURANT_OWNER);

        long totalRestaurants = restaurantRepository.count();

        long pendingRestaurants =
                restaurantRepository.countByStatus(RestaurantStatus.PENDING);

        long approvedRestaurants =
                restaurantRepository.countByStatus(RestaurantStatus.APPROVED);

        long rejectedRestaurants =
                restaurantRepository.countByStatus(RestaurantStatus.REJECTED);

        long totalOrders = customerOrderRepository.count();

        long pendingOrders =
                customerOrderRepository.countByStatus(OrderStatus.PENDING);

        long deliveredOrders =
                customerOrderRepository.countByStatus(OrderStatus.DELIVERED);

        long cancelledOrders =
                customerOrderRepository.countByStatus(OrderStatus.CANCELLED);

        BigDecimal totalRevenue =
                paymentRepository.calculateTotalRevenue(PaymentStatus.PAID);

        return new AdminDashboardResponse(
                totalUsers,
                totalCustomers,
                totalRestaurantOwners,
                totalRestaurants,
                pendingRestaurants,
                approvedRestaurants,
                rejectedRestaurants,
                totalOrders,
                pendingOrders,
                deliveredOrders,
                cancelledOrders,
                totalRevenue
        );
    }
}