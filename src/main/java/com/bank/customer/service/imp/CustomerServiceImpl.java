package com.bank.customer.service.imp;

import com.bank.customer.api.dto.CustomerRequest;
import com.bank.customer.api.dto.CustomerResponse;
import com.bank.customer.domain.Customer;
import com.bank.customer.exception.CustomerAlreadyExistsException;
import com.bank.customer.exception.CustomerNotFoundException;
import com.bank.customer.mapper.CustomerMapper;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.service.CustomerService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Single<CustomerResponse> create(CustomerRequest request) {
        return Single.fromPublisher(
                customerRepository.existsByDocumentNumber(request.getDocumentNumber())
                        .flatMap(exists -> {
                            if (Boolean.TRUE.equals(exists)) {
                                return Mono.error(
                                        new CustomerAlreadyExistsException(request.getDocumentNumber())
                                );
                            }

                            return customerRepository.save(customerMapper.toModel(request));
                        })
                        .map(customerMapper::toResponse)
        );
    }

    @Override
    public Flowable<CustomerResponse> findAll() {
        log.info("Finding all customers");

        return Flowable.fromPublisher(customerRepository.findAll())
                .map(customerMapper::toResponse)
                .doOnComplete(() -> log.info("Customers found successfully"))
                .doOnError(error -> log.error("Error finding customers: {}", error.getMessage()));
    }

    @Override
    public Single<CustomerResponse> findById(String id) {
        log.info("Finding customer by id: {}", id);

        return Single.fromPublisher(
                        customerRepository.findById(id)
                                .switchIfEmpty(reactor.core.publisher.Mono.error(new CustomerNotFoundException(id)))
                )
                .map(customerMapper::toResponse)
                .doOnSuccess(response -> log.info("Customer found with id: {}", id))
                .doOnError(error -> log.error("Error finding customer with id {}: {}", id, error.getMessage()));
    }

    @Override
    public Single<CustomerResponse> update(String id, CustomerRequest request) {
        return Single.fromPublisher(
                        customerRepository.findById(id)
                                .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)))
                )
                .flatMap(existingCustomer ->
                        validateDocumentNumberForUpdate(id, request)
                                .flatMap(valid -> {
                                    Customer updatedCustomer =
                                            customerMapper.updateEntity(existingCustomer, request);

                                    return Single.fromPublisher(customerRepository.save(updatedCustomer))
                                            .map(customerMapper::toResponse);
                                })
                );
    }

    private Single<Boolean> validateDocumentNumberForUpdate(
            String id,
            CustomerRequest request
    ) {
        return Single.fromPublisher(
                        customerRepository.findByDocumentNumber(request.getDocumentNumber())
                                .filter(customer -> !customer.getId().equals(id))
                                .hasElement()
                )
                .flatMap(existsInAnotherCustomer -> {
                    if (Boolean.TRUE.equals(existsInAnotherCustomer)) {
                        return Single.error(
                                new CustomerAlreadyExistsException(request.getDocumentNumber())
                        );
                    }

                    return Single.just(Boolean.TRUE);
                });
    }

}