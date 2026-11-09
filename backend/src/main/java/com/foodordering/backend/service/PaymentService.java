package com.foodordering.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.PaymentResponse;
import com.foodordering.backend.entity.OrderStatus;
import com.foodordering.backend.entity.Payment;
import com.foodordering.backend.entity.PaymentMethod;
import com.foodordering.backend.entity.PaymentStatus;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            Long orderId,
            User customer
    ) {

        // Step 1: Check whether the logged-in user is a customer.
        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can view their payment details"
            );
        }

        // Step 2: Find the payment associated with the order.
        Payment payment = paymentRepository
                .findByCustomerOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found for this order"
                ));

        // Step 3: Check whether the order belongs to this customer.
        if (!payment.getCustomerOrder()
                .getCustomer()
                .getId()
                .equals(customer.getId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to view this payment"
            );
        }

        // Step 4: Return payment details.
        return new PaymentResponse(payment);
    }

    @Transactional
    public PaymentResponse markPaymentAsPaid(
            Long orderId,
            User owner
    ) {

        // Step 1: Check whether the logged-in user is a restaurant owner.
        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new IllegalArgumentException(
                    "Only restaurant owners can update payment status"
            );
        }

        // Step 2: Find the payment associated with the order.
        Payment payment = paymentRepository
                .findByCustomerOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found for this order"
                ));

        // Step 3: Verify that the order belongs to this restaurant owner.
        if (!payment.getCustomerOrder()
                .getRestaurant()
                .getOwner()
                .getId()
                .equals(owner.getId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to update this payment"
            );
        }

        // Step 4: Check whether the payment method is COD.
        if (payment.getPaymentMethod() != PaymentMethod.CASH_ON_DELIVERY) {
            throw new IllegalArgumentException(
                    "This operation is only available for Cash on Delivery"
            );
        }

        // Step 5: Check whether the order has been delivered.
        if (payment.getCustomerOrder().getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException(
                    "Payment can only be collected after order delivery"
            );
        }

        // Step 6: Check whether payment is already completed.
        if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Payment is not in PENDING status"
            );
        }

        // Step 7: Update payment status.
        payment.setPaymentStatus(PaymentStatus.PAID);

        // Step 8: Save the updated payment.
        Payment updatedPayment = paymentRepository.save(payment);

        // Step 9: Return updated payment details.
        return new PaymentResponse(updatedPayment);
    }
}