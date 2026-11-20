package com.foodordering.backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodordering.backend.dto.MonthlyRevenueResponse;
import com.foodordering.backend.entity.Payment;
import com.foodordering.backend.entity.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByCustomerOrderId(Long orderId);

    boolean existsByCustomerOrderId(Long orderId);

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.paymentStatus = :status
            """)
    BigDecimal calculateTotalRevenue(
            @Param("status") PaymentStatus status
    );

    @Query("""
            SELECT new com.foodordering.backend.dto.MonthlyRevenueResponse(
                YEAR(p.updatedAt),
                MONTH(p.updatedAt),
                SUM(p.amount)
            )
            FROM Payment p
            WHERE p.paymentStatus = :status
            GROUP BY YEAR(p.updatedAt), MONTH(p.updatedAt)
            ORDER BY YEAR(p.updatedAt), MONTH(p.updatedAt)
            """)
    List<MonthlyRevenueResponse> findMonthlyRevenue(
            @Param("status") PaymentStatus status
    );

}