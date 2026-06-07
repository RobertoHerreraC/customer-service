package com.bank.customer.strategy;

import com.bank.customer.domain.CustomerProfile;
import com.bank.customer.domain.CustomerType;
import com.bank.customer.exception.CustomerBusinessException;
import org.springframework.stereotype.Component;

@Component
public class PersonalCustomerProfileValidationStrategy implements CustomerProfileValidationStrategy {
    @Override
    public boolean supports(CustomerType customerType) {
        return CustomerType.PERSONAL.equals(customerType);
    }

    @Override
    public void validate(CustomerProfile profile) {
        if (CustomerProfile.PYME.equals(profile)) {
            throw new CustomerBusinessException("Personal customer cannot have PYME profile");
        }
    }
}
