
package com.foodordering.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.CouponRedemption;

public interface CouponRedemptionRepository
        extends JpaRepository<CouponRedemption, Long> {

    // Check whether a customer has already used a coupon.
    boolean existsByCoupon_IdAndCustomer_Id(
            Long couponId,
            Long customerId
    );

    // Count the total number of redemptions for a coupon.
    long countByCoupon_Id(Long couponId);

    // Find the redemption record associated with an order.
    Optional<CouponRedemption> findByCustomerOrder_Id(Long orderId);
}
