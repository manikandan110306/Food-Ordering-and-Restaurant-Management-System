package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodordering.backend.dto.RestaurantPerformanceResponse;
import com.foodordering.backend.entity.CustomerOrder;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.entity.PaymentStatus;

public interface CustomerOrderRepository
        extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<CustomerOrder> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);

    Optional<CustomerOrder> findByIdAndCustomerId(
            Long id,
            Long customerId
    );

    Optional<CustomerOrder> findByIdAndRestaurantId(
            Long id,
            Long restaurantId
    );

    List<CustomerOrder> findByRestaurant_Owner_IdOrderByCreatedAtDesc(
            Long ownerId
    );

    Optional<CustomerOrder> findByIdAndRestaurant_Owner_Id(
            Long orderId,
            Long ownerId
    );

    long countByStatus(OrderStatus status);

    @Query("""
            SELECT new com.foodordering.backend.dto.RestaurantPerformanceResponse(
                o.restaurant.id,
                o.restaurant.name,
                o.restaurant.address,
                COUNT(o.id),
                COUNT(CASE WHEN o.status = :deliveredStatus THEN o.id END),
                COALESCE(SUM(p.amount), 0)
            )
            FROM CustomerOrder o
            LEFT JOIN Payment p
                ON p.customerOrder = o
                AND p.paymentStatus = :paidStatus
            GROUP BY o.restaurant.id, o.restaurant.name, o.restaurant.address
            ORDER BY SUM(p.amount) DESC
            """)
    List<RestaurantPerformanceResponse> findRestaurantPerformance(
            @Param("deliveredStatus") OrderStatus deliveredStatus,
            @Param("paidStatus") PaymentStatus paidStatus
    );

}