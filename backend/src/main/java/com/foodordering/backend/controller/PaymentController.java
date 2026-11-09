package com.foodordering.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.PaymentResponse;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Customer retrieves payment details.
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(
            @PathVariable Long orderId,
            Authentication authentication
    ) {

        try {

            User customer = (User) authentication.getPrincipal();

            PaymentResponse response = paymentService.getPaymentByOrderId(
                    orderId,
                    customer
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest().body(
                    exception.getMessage()
            );
        }
    }

    // Restaurant owner marks COD payment as paid.
    @PatchMapping("/order/{orderId}/collect")
    public ResponseEntity<?> markPaymentAsPaid(
            @PathVariable Long orderId,
            Authentication authentication
    ) {

        try {

            // Get the currently logged-in restaurant owner.
            User owner = (User) authentication.getPrincipal();

            // Mark the payment as paid through the service.
            PaymentResponse response = paymentService.markPaymentAsPaid(
                    orderId,
                    owner
            );

            // Return the updated payment details.
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest().body(
                    exception.getMessage()
            );
        }
    }
}