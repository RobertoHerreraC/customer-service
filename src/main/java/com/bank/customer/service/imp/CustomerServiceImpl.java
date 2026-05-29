package com.bank.customer.service.imp;

import com.bank.customer.api.dto.CustomerRequest;
import com.bank.customer.api.dto.CustomerResponse;
import com.bank.customer.exception.CustomerAlreadyExistsException;
import com.bank.customer.mapper.CustomerMapper;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.service.CustomerService;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
}