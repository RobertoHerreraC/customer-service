package com.bank.customer.controller;

import com.bank.customer.api.dto.CustomerRequest;
import com.bank.customer.api.dto.CustomerResponse;
import com.bank.customer.api.generated.CustomersApi;
import com.bank.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomersApi {

    private final CustomerService customerService;

    @Override
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(
            Mono<CustomerRequest> customerRequest,
            ServerWebExchange exchange) {

        return customerRequest
                .flatMap(request ->
                        Mono.fromCompletionStage(
                                customerService.create(request)
                                        .toCompletionStage()
                        ))
                .map(response ->
                        ResponseEntity.status(HttpStatus.CREATED)
                                .body(response)
                );
    }
}