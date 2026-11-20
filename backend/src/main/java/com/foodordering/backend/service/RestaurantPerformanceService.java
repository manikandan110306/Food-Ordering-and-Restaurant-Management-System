package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.RestaurantPerformanceResponse;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.entity.PaymentStatus;
import com.foodordering.backend.repository.CustomerOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantPerformanceService {

    private final CustomerOrderRepository customerOrderRepository;

    public List<RestaurantPerformanceResponse> getRestaurantPerformance() {

        return customerOrderRepository.findRestaurantPerformance(
                OrderStatus.DELIVERED,
                PaymentStatus.PAID
        );
    }
}