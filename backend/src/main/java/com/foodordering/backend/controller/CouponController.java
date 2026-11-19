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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.CouponRequest;
import com.foodordering.backend.dto.CouponResponse;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // Admin: Create a new coupon.
    @PostMapping("/admin/coupons")
    public ResponseEntity<?> createCoupon(
            @Valid @RequestBody CouponRequest request,
            Authentication authentication) {

        try {

            User admin = (User) authentication.getPrincipal();

            if (admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Only admins can create coupons"));
            }

            CouponResponse response = couponService.createCoupon(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    // Admin: View all coupons.
    @GetMapping("/admin/coupons")
    public ResponseEntity<?> getAllCoupons(
            Authentication authentication) {

        User admin = (User) authentication.getPrincipal();

        if (admin.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admins can view all coupons"));
        }

        List<CouponResponse> coupons = couponService.getAllCoupons();

        return ResponseEntity.ok(coupons);
    }

    // Admin: Activate or deactivate a coupon.
    @PatchMapping("/admin/coupons/{couponId}/status")
    public ResponseEntity<?> updateCouponStatus(
            @PathVariable Long couponId,
            @RequestParam boolean active,
            Authentication authentication) {

        try {

            User admin = (User) authentication.getPrincipal();

            if (admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Only admins can update coupon status"));
            }

            CouponResponse response =
                    couponService.updateCouponStatus(couponId, active);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }

    // Public: View currently available coupons.
    @GetMapping("/coupons/available")
    public ResponseEntity<List<CouponResponse>> getAvailableCoupons() {

        List<CouponResponse> coupons = couponService.getAvailableCoupons();

        return ResponseEntity.ok(coupons);
    }
}