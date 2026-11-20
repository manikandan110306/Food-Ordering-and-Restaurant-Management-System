package com.foodordering.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodordering.backend.dto.TopSellingFoodResponse;
import com.foodordering.backend.entity.OrderItem;
import com.foodordering.backend.entity.OrderStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByCustomerOrderId(Long orderId);

    @Query("""
        SELECT new com.foodordering.backend.dto.TopSellingFoodResponse(
            oi.foodName,
            SUM(oi.quantity),
            SUM(oi.subtotal)
        )
        FROM OrderItem oi
        WHERE oi.customerOrder.status <> :cancelledStatus
        GROUP BY oi.foodName
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<TopSellingFoodResponse> findTopSellingFoodItems(
        @Param("cancelledStatus") OrderStatus cancelledStatus
    );
}