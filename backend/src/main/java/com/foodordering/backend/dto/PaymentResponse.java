package com.foodordering.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.foodordering.backend.entity.Payment;
import com.foodordering.backend.entity.PaymentMethod;
import com.foodordering.backend.entity.PaymentStatus;

import lombok.Getter;

@Getter
public class PaymentResponse {

    private Long paymentId;

    private Long orderId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PaymentResponse(Payment payment) {

        this.paymentId = payment.getId();

        this.orderId = payment.getCustomerOrder().getId();

        this.amount = payment.getAmount();

        this.paymentMethod = payment.getPaymentMethod();

        this.paymentStatus = payment.getPaymentStatus();

        this.createdAt = payment.getCreatedAt();

        this.updatedAt = payment.getUpdatedAt();
    }
}