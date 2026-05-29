package com.bank.customer.repository;

import com.bank.customer.api.dto.CustomerResponse;
import com.bank.customer.domain.Customer;
import io.reactivex.rxjava3.core.Single;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Mono<Boolean> existsByDocumentNumber(String documentNumber);
    Mono<Customer> findByDocumentNumber(String documentNumber);
}