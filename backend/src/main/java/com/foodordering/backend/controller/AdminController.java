package com.foodordering.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.foodordering.backend.dto.CouponUsageResponse;
import com.foodordering.backend.service.CouponUsageService;

import com.foodordering.backend.dto.AdminDashboardResponse;
import com.foodordering.backend.dto.MonthlyRevenueResponse;
import com.foodordering.backend.dto.RestaurantPerformanceResponse;
import com.foodordering.backend.dto.RestaurantResponse;
import com.foodordering.backend.dto.TopSellingFoodResponse;
import com.foodordering.backend.entity.Restaurant;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.AdminDashboardService;
import com.foodordering.backend.service.MonthlyRevenueService;
import com.foodordering.backend.service.RestaurantPerformanceService;
import com.foodordering.backend.service.RestaurantService;
import com.foodordering.backend.service.TopSellingFoodService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final RestaurantService restaurantService;

    private final AdminDashboardService adminDashboardService;

    private final MonthlyRevenueService monthlyRevenueService;

    private final RestaurantPerformanceService restaurantPerformanceService;

    private final TopSellingFoodService topSellingFoodService;

    private final CouponUsageService couponUsageService;

    public AdminController(
            RestaurantService restaurantService,
            AdminDashboardService adminDashboardService,
            MonthlyRevenueService monthlyRevenueService,
            RestaurantPerformanceService restaurantPerformanceService,
            TopSellingFoodService topSellingFoodService,
            CouponUsageService couponUsageService) {

        this.restaurantService = restaurantService;
        this.adminDashboardService = adminDashboardService;
        this.monthlyRevenueService = monthlyRevenueService;
        this.restaurantPerformanceService = restaurantPerformanceService;
        this.topSellingFoodService = topSellingFoodService;
        this.couponUsageService = couponUsageService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStatistics(
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can access the dashboard");
        }

        AdminDashboardResponse response = adminDashboardService.getDashboardStatistics();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/monthly-revenue")
    public ResponseEntity<?> getMonthlyRevenue(
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can access monthly revenue reports");
        }

        List<MonthlyRevenueResponse> response = monthlyRevenueService.getMonthlyRevenue();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/restaurant-performance")
    public ResponseEntity<?> getRestaurantPerformance(
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can access restaurant performance reports");
        }

        List<RestaurantPerformanceResponse> response = restaurantPerformanceService.getRestaurantPerformance();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/top-selling-food")
    public ResponseEntity<?> getTopSellingFoodItems(
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can access top-selling food reports");
        }

        List<TopSellingFoodResponse> response = topSellingFoodService.getTopSellingFoodItems();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants/pending")
    public ResponseEntity<?> getPendingRestaurants(
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can view pending restaurants");
        }

        List<Restaurant> restaurants = restaurantService.getPendingRestaurants();

        List<RestaurantResponse> response = restaurants.stream()
                .map(RestaurantResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/restaurants/{id}/approve")
    public ResponseEntity<?> approveRestaurant(
            @PathVariable Long id,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can approve restaurants");
        }

        try {
            Restaurant restaurant = restaurantService.approveRestaurant(id);

            RestaurantResponse response = new RestaurantResponse(restaurant);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @PutMapping("/restaurants/{id}/reject")
    public ResponseEntity<?> rejectRestaurant(
            @PathVariable Long id,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can reject restaurants");
        }

        try {
            Restaurant restaurant = restaurantService.rejectRestaurant(id);

            RestaurantResponse response = new RestaurantResponse(restaurant);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @GetMapping("/reports/coupon-usage")
    public ResponseEntity<?> getCouponUsageReport(
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can access coupon usage reports");
        }

        List<CouponUsageResponse> response = couponUsageService.getCouponUsageReport();

        return ResponseEntity.ok(response);
    }

}