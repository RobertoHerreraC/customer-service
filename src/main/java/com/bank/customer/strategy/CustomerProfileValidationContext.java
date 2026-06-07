package com.bank.customer.strategy;

import com.bank.customer.domain.CustomerProfile;
import com.bank.customer.domain.CustomerType;
import com.bank.customer.exception.CustomerProfileInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerProfileValidationContext {
    private final List<CustomerProfileValidationStrategy> strategies;
    public void validate(CustomerType customerType, CustomerProfile profile) {
        CustomerProfile safeProfile = profile == null ? CustomerProfile.STANDARD : profile;

        strategies.stream()
                .filter(strategy -> strategy.supports(customerType))
                .findFirst()
                .orElseThrow(() -> new CustomerProfileInvalidException("Unsupported customer type"))
                .validate(safeProfile);
    }
}
