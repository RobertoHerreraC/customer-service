package com.bank.customer.strategy;

import com.bank.customer.domain.CustomerProfile;
import com.bank.customer.domain.CustomerType;
import com.bank.customer.exception.CustomerBusinessException;
import org.springframework.stereotype.Component;

@Component
public class BusinessCustomerProfileValidationStrategy  implements CustomerProfileValidationStrategy {
    @Override
    public boolean supports(CustomerType customerType) {
        return CustomerType.BUSINESS.equals(customerType);
    }

    @Override
    public void validate(CustomerProfile profile) {
        if (CustomerProfile.VIP.equals(profile)) {
            throw new CustomerBusinessException("Business customer cannot have VIP profile");
        }
    }
}
