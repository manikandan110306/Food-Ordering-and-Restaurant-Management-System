package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.AddressRequest;
import com.foodordering.backend.dto.AddressResponse;
import com.foodordering.backend.entity.CustomerAddress;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.CustomerAddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerAddressService {

    private final CustomerAddressRepository addressRepository;

    // Add a new delivery address
    @Transactional
    public AddressResponse addAddress(AddressRequest request, User customer) {

        validateCustomer(customer);

        List<CustomerAddress> existingAddresses =
                addressRepository.findByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                        customer.getId()
                );

        CustomerAddress address = new CustomerAddress();

        address.setUser(customer);

        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setLandmark(request.getLandmark());

        boolean makeDefault =
                request.isDefaultAddress() || existingAddresses.isEmpty();

        if (makeDefault) {
            removeExistingDefaultAddress(customer.getId());
        }

        address.setDefaultAddress(makeDefault);

        CustomerAddress savedAddress = addressRepository.save(address);

        return new AddressResponse(savedAddress);
    }

    // Get all addresses belonging to the logged-in customer
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(User customer) {

        validateCustomer(customer);

        return addressRepository
                .findByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                        customer.getId()
                )
                .stream()
                .map(AddressResponse::new)
                .toList();
    }

    // Update an existing delivery address
    @Transactional
    public AddressResponse updateAddress(
            Long addressId,
            AddressRequest request,
            User customer
    ) {

        validateCustomer(customer);

        CustomerAddress address = addressRepository
                .findByIdAndUserId(addressId, customer.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Address not found or you do not have permission to update it"
                        )
                );

        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setLandmark(request.getLandmark());

        if (request.isDefaultAddress()) {

            removeExistingDefaultAddress(customer.getId());

            address.setDefaultAddress(true);

        } else {

            address.setDefaultAddress(false);
        }

        CustomerAddress updatedAddress = addressRepository.save(address);

        return new AddressResponse(updatedAddress);
    }

    // Delete an existing delivery address
    @Transactional
    public void deleteAddress(Long addressId, User customer) {

        validateCustomer(customer);

        CustomerAddress address = addressRepository
                .findByIdAndUserId(addressId, customer.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Address not found or you do not have permission to delete it"
                        )
                );

        boolean wasDefault = address.isDefaultAddress();

        addressRepository.delete(address);

        if (wasDefault) {

            List<CustomerAddress> remainingAddresses =
                    addressRepository.findByUserIdOrderByDefaultAddressDescCreatedAtDesc(
                            customer.getId()
                    );

            if (!remainingAddresses.isEmpty()) {

                CustomerAddress newDefault = remainingAddresses.get(0);

                newDefault.setDefaultAddress(true);

                addressRepository.save(newDefault);
            }
        }
    }

    // Set a particular address as the default address
    @Transactional
    public AddressResponse setDefaultAddress(Long addressId, User customer) {

        validateCustomer(customer);

        CustomerAddress address = addressRepository
                .findByIdAndUserId(addressId, customer.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Address not found or you do not have permission to update it"
                        )
                );

        removeExistingDefaultAddress(customer.getId());

        address.setDefaultAddress(true);

        CustomerAddress updatedAddress = addressRepository.save(address);

        return new AddressResponse(updatedAddress);
    }

    // Remove the previous default address
    private void removeExistingDefaultAddress(Long userId) {

        addressRepository.findByUserIdAndDefaultAddressTrue(userId)
                .ifPresent(existingAddress -> {

                    existingAddress.setDefaultAddress(false);

                    addressRepository.save(existingAddress);
                });
    }

    // Ensure only customers can manage delivery addresses
    private void validateCustomer(User customer) {

        if (customer == null || customer.getRole() != Role.CUSTOMER) {

            throw new IllegalArgumentException(
                    "Only customers can manage delivery addresses"
            );
        }
    }
}