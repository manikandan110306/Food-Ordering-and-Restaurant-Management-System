package com.foodordering.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.foodordering.backend.dto.AddressRequest;
import com.foodordering.backend.dto.AddressResponse;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.CustomerAddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class CustomerAddressController {

    private final CustomerAddressService addressService;

    // Add a new delivery address
    @PostMapping
    public ResponseEntity<?> addAddress(
            @Valid @RequestBody AddressRequest request,
            @AuthenticationPrincipal User customer
    ) {

        try {

            AddressResponse response =
                    addressService.addAddress(request, customer);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Get all saved addresses
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(
            @AuthenticationPrincipal User customer
    ) {

        List<AddressResponse> addresses =
                addressService.getAddresses(customer);

        return ResponseEntity.ok(addresses);
    }

    // Update an existing address
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request,
            @AuthenticationPrincipal User customer
    ) {

        try {

            AddressResponse response =
                    addressService.updateAddress(
                            addressId,
                            request,
                            customer
                    );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Delete an address
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal User customer
    ) {

        try {

            addressService.deleteAddress(addressId, customer);

            return ResponseEntity.ok(
                    "Address deleted successfully"
            );

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Set an address as the default address
    @PutMapping("/{addressId}/default")
    public ResponseEntity<?> setDefaultAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal User customer
    ) {

        try {

            AddressResponse response =
                    addressService.setDefaultAddress(
                            addressId,
                            customer
                    );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }
}