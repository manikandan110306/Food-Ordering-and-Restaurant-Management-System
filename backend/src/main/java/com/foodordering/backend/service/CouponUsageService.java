package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.CouponUsageResponse;
import com.foodordering.backend.repository.CouponRedemptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponUsageService {

    private final CouponRedemptionRepository couponRedemptionRepository;

    public List<CouponUsageResponse> getCouponUsageReport() {

        return couponRedemptionRepository.findCouponUsageReport();
    }
}