package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.MonthlyRevenueResponse;
import com.foodordering.backend.entity.PaymentStatus;
import com.foodordering.backend.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonthlyRevenueService {

    private final PaymentRepository paymentRepository;

    public List<MonthlyRevenueResponse> getMonthlyRevenue() {

        return paymentRepository.findMonthlyRevenue(
                PaymentStatus.PAID
        );
    }
}