
package com.foodordering.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.CouponRequest;
import com.foodordering.backend.dto.CouponResponse;

import com.foodordering.backend.entity.Coupon;
import com.foodordering.backend.entity.DiscountType;
import com.foodordering.backend.entity.NotificationType;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;

import com.foodordering.backend.repository.CouponRepository;
import com.foodordering.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    private final NotificationService notificationService;

    // Create a new coupon.

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {

        // Step 1: Normalize the coupon code.

        String code = request.getCode()
                .trim()
                .toUpperCase(Locale.ROOT);

        // Step 2: Check whether the coupon code already exists.

        if (couponRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException(
                    "Coupon code already exists");
        }

        // Step 3: Validate the coupon details.

        validateCoupon(request);

        // Step 4: Create a new coupon entity.

        Coupon coupon = new Coupon();

        coupon.setCode(code);

        coupon.setDescription(request.getDescription());

        coupon.setDiscountType(request.getDiscountType());

        coupon.setDiscountValue(request.getDiscountValue());

        coupon.setMinimumOrderAmount(
                request.getMinimumOrderAmount() == null
                        ? BigDecimal.ZERO
                        : request.getMinimumOrderAmount()
        );

        coupon.setMaximumDiscount(request.getMaximumDiscount());

        coupon.setStartDate(request.getStartDate());

        coupon.setExpiryDate(request.getExpiryDate());

        coupon.setUsageLimit(request.getUsageLimit());

        coupon.setUsedCount(0);

        coupon.setActive(request.isActive());

        // Step 5: Save the coupon.

        Coupon savedCoupon = couponRepository.save(coupon);

        // Step 6: Notify customers if the coupon is currently available.

        LocalDateTime now = LocalDateTime.now();

        boolean couponAvailable = savedCoupon.isActive()
                && !now.isBefore(savedCoupon.getStartDate())
                && !now.isAfter(savedCoupon.getExpiryDate());

        if (couponAvailable) {

            List<User> customers = userRepository.findAll()
                    .stream()
                    .filter(user -> user.getRole() == Role.CUSTOMER)
                    .toList();

            for (User customer : customers) {

                notificationService.createNotification(
                        customer,
                        "New Special Offer!",
                        "A new coupon " + savedCoupon.getCode()
                                + " is available. Apply it during checkout "
                                + "to enjoy a discount.",
                        NotificationType.PROMOTION
                );
            }
        }

        // Step 7: Return the response.

        return new CouponResponse(savedCoupon);
    }

    // Retrieve all coupons for admin.

    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {

        return couponRepository.findAll()
                .stream()
                .map(CouponResponse::new)
                .toList();
    }

    // Retrieve coupons currently available to customers.

    @Transactional(readOnly = true)
    public List<CouponResponse> getAvailableCoupons() {

        LocalDateTime now = LocalDateTime.now();

        return couponRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .filter(coupon ->
                        !now.isBefore(coupon.getStartDate())
                        && !now.isAfter(coupon.getExpiryDate())
                        && coupon.getUsedCount() < coupon.getUsageLimit()
                )
                .map(CouponResponse::new)
                .toList();
    }

    // Activate or deactivate a coupon.

    @Transactional
    public CouponResponse updateCouponStatus(Long couponId, boolean active) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Coupon not found"));

        coupon.setActive(active);

        Coupon updatedCoupon = couponRepository.save(coupon);

        return new CouponResponse(updatedCoupon);
    }

    // Validate coupon details before saving.

    private void validateCoupon(CouponRequest request) {

        // Validate discount type.

        if (request.getDiscountType() == null) {
            throw new IllegalArgumentException(
                    "Discount type is required");
        }

        // Validate discount value.

        if (request.getDiscountValue() == null
                || request.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Discount value must be greater than zero");
        }

        // Percentage discounts cannot exceed 100%.

        if (request.getDiscountType() == DiscountType.PERCENTAGE
                && request.getDiscountValue()
                        .compareTo(new BigDecimal("100")) > 0) {

            throw new IllegalArgumentException(
                    "Percentage discount cannot exceed 100");
        }

        // Validate minimum order amount.

        if (request.getMinimumOrderAmount() != null
                && request.getMinimumOrderAmount()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Minimum order amount cannot be negative");
        }

        // Maximum discount is allowed only for percentage coupons.

        if (request.getDiscountType() == DiscountType.FIXED
                && request.getMaximumDiscount() != null) {

            throw new IllegalArgumentException(
                    "Maximum discount is only applicable to percentage coupons");
        }

        if (request.getDiscountType() == DiscountType.PERCENTAGE
                && request.getMaximumDiscount() != null
                && request.getMaximumDiscount()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Maximum discount must be greater than zero");
        }

        // Validate dates.

        if (request.getStartDate() == null
                || request.getExpiryDate() == null) {

            throw new IllegalArgumentException(
                    "Coupon start date and expiry date are required");
        }

        if (!request.getExpiryDate().isAfter(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "Coupon expiry date must be after its start date");
        }

        // Validate usage limit.

        if (request.getUsageLimit() == null
                || request.getUsageLimit() <= 0) {

            throw new IllegalArgumentException(
                    "Coupon usage limit must be greater than zero");
        }
    }
}
