package com.bank.customer.repository;

import com.bank.customer.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Mono<Boolean> existsByDocumentNumber(String documentNumber);
}