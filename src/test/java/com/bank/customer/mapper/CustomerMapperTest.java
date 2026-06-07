package com.bank.customer.mapper;

import com.bank.customer.api.dto.CustomerRequest;
import com.bank.customer.api.dto.CustomerResponse;
import com.bank.customer.domain.Customer;
import com.bank.customer.domain.CustomerProfile;
import com.bank.customer.domain.CustomerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerMapperTest {
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void shouldMapRequestToModelWithVipProfile() {
        CustomerRequest request = new CustomerRequest();
        request.setDocumentNumber("12345678");
        request.setFullName("Luis Torres");
        request.setCustomerType("PERSONAL");
        request.setProfile("VIP");

        Customer customer = customerMapper.toModel(request);

        assertNotNull(customer);
        assertEquals("12345678", customer.getDocumentNumber());
        assertEquals("Luis Torres", customer.getFullName());
        assertEquals(CustomerType.PERSONAL, customer.getCustomerType());
        assertEquals(CustomerProfile.VIP, customer.getCustomerProfile());
    }

    @Test
    void shouldMapRequestToModelWithStandardProfileWhenProfileIsNull() {
        CustomerRequest request = new CustomerRequest();
        request.setDocumentNumber("12345678");
        request.setFullName("Luis Torres");
        request.setCustomerType("PERSONAL");
        request.setProfile(null);

        Customer customer = customerMapper.toModel(request);

        assertEquals(CustomerProfile.STANDARD, customer.getCustomerProfile());
    }

    @Test
    void shouldMapModelToResponse() {
        Customer customer = Customer.builder()
                .id("customer-id")
                .documentNumber("12345678")
                .fullName("Luis Torres")
                .customerType(CustomerType.PERSONAL)
                .customerProfile(CustomerProfile.VIP)
                .build();

        CustomerResponse response = customerMapper.toResponse(customer);

        assertNotNull(response);
        assertEquals("customer-id", response.getId());
        assertEquals("12345678", response.getDocumentNumber());
        assertEquals("Luis Torres", response.getFullName());
        assertEquals("PERSONAL", response.getCustomerType());
        assertEquals("VIP", response.getProfile());
    }

    @Test
    void shouldUpdateEntity() {
        Customer existingCustomer = Customer.builder()
                .id("customer-id")
                .documentNumber("12345678")
                .fullName("Old Name")
                .customerType(CustomerType.PERSONAL)
                .customerProfile(CustomerProfile.STANDARD)
                .build();

        CustomerRequest request = new CustomerRequest();
        request.setDocumentNumber("12345678");
        request.setFullName("New Name");
        request.setCustomerType("PERSONAL");
        request.setProfile("VIP");

        Customer updatedCustomer = customerMapper.updateEntity(existingCustomer, request);

        assertEquals("customer-id", updatedCustomer.getId());
        assertEquals("New Name", updatedCustomer.getFullName());
        assertEquals(CustomerType.PERSONAL, updatedCustomer.getCustomerType());
        assertEquals(CustomerProfile.VIP, updatedCustomer.getCustomerProfile());
    }
}
