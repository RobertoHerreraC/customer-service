package com.bank.customer.mapper;

import com.bank.customer.api.dto.CustomerRequest;
import com.bank.customer.api.dto.CustomerResponse;
import com.bank.customer.domain.Customer;
import com.bank.customer.domain.CustomerType;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toModel(CustomerRequest request) {
        return Customer.builder()
                .documentNumber(request.getDocumentNumber())
                .fullName(request.getFullName())
                .customerType(CustomerType.valueOf(request.getCustomerType()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .active(true)
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setDocumentNumber(customer.getDocumentNumber());
        response.setFullName(customer.getFullName());
        response.setCustomerType(customer.getCustomerType().toString());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setActive(customer.getActive());
        return response;
    }

    public Customer updateEntity(Customer customer, CustomerRequest request) {
        customer.setDocumentNumber(request.getDocumentNumber());
        customer.setFullName(request.getFullName());
        customer.setCustomerType(CustomerType.valueOf(request.getCustomerType()));
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return customer;
    }
}