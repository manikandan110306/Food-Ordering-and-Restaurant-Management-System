package com.foodordering.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByCustomerOrderId(Long orderId);

    boolean existsByCustomerOrderId(Long orderId);

}