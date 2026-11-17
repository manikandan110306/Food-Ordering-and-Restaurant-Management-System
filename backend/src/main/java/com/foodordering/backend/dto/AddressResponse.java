package com.foodordering.backend.dto;

import java.time.LocalDateTime;

import com.foodordering.backend.entity.CustomerAddress;

import lombok.Getter;

@Getter
public class AddressResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String postalCode;

    private String landmark;

    private boolean defaultAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AddressResponse(CustomerAddress address) {

        this.id = address.getId();

        this.fullName = address.getFullName();

        this.phone = address.getPhone();

        this.addressLine1 = address.getAddressLine1();

        this.addressLine2 = address.getAddressLine2();

        this.city = address.getCity();

        this.state = address.getState();

        this.postalCode = address.getPostalCode();

        this.landmark = address.getLandmark();

        this.defaultAddress = address.isDefaultAddress();

        this.createdAt = address.getCreatedAt();

        this.updatedAt = address.getUpdatedAt();
    }
}