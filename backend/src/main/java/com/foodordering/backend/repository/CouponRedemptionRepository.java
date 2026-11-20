package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodordering.backend.dto.CouponUsageResponse;
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

    // Generate coupon usage statistics.
    @Query("""
        SELECT new com.foodordering.backend.dto.CouponUsageResponse(
            cr.coupon.code,
            COUNT(cr.id),
            SUM(cr.customerOrder.discountAmount)
        )
        FROM CouponRedemption cr
        GROUP BY cr.coupon.code
        ORDER BY COUNT(cr.id) DESC
    """)
    List<CouponUsageResponse> findCouponUsageReport();
}