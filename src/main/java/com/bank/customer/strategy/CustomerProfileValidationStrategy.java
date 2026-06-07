package com.bank.customer.strategy;

import com.bank.customer.domain.CustomerProfile;
import com.bank.customer.domain.CustomerType;

public interface CustomerProfileValidationStrategy {
    boolean supports(CustomerType customerType);
    void validate(CustomerProfile profile);
}
