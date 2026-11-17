package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.CustomerAddress;

public interface CustomerAddressRepository
        extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findByUserIdOrderByDefaultAddressDescCreatedAtDesc(
            Long userId
    );

    Optional<CustomerAddress> findByIdAndUserId(
            Long id,
            Long userId
    );

    Optional<CustomerAddress> findByUserIdAndDefaultAddressTrue(
            Long userId
    );

    boolean existsByUserId(Long userId);
}