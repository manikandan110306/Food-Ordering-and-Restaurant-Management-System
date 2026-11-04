package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.CustomerOrder;

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
}