package com.bank.customer.service;

import com.bank.customer.api.dto.CustomerRequest;
import com.bank.customer.api.dto.CustomerResponse;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface CustomerService {

    Single<CustomerResponse> create(CustomerRequest request);
    Flowable<CustomerResponse> findAll();
    Single<CustomerResponse> findById(String id);
}