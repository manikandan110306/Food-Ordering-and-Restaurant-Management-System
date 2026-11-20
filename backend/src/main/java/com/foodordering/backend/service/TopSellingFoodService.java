package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.TopSellingFoodResponse;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopSellingFoodService {

    private final OrderItemRepository orderItemRepository;

    public List<TopSellingFoodResponse> getTopSellingFoodItems() {

        return orderItemRepository.findTopSellingFoodItems(
            OrderStatus.CANCELLED
        );
    }
}