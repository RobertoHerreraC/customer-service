package com.bank.customer.strategy;

import com.bank.customer.domain.CustomerProfile;
import com.bank.customer.domain.CustomerType;
import com.bank.customer.exception.CustomerBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerProfileValidationContextTest {
    private CustomerProfileValidationContext context;

    @BeforeEach
    void setUp() {
        context = new CustomerProfileValidationContext(
                List.of(
                        new PersonalCustomerProfileValidationStrategy(),
                        new BusinessCustomerProfileValidationStrategy()
                )
        );
    }

    @Test
    void shouldValidatePersonalStandardProfile() {
        assertDoesNotThrow(() ->
                context.validate(CustomerType.PERSONAL, CustomerProfile.STANDARD));
    }

    @Test
    void shouldValidatePersonalVipProfile() {
        assertDoesNotThrow(() ->
                context.validate(CustomerType.PERSONAL, CustomerProfile.VIP));
    }

    @Test
    void shouldThrowWhenPersonalHasPymeProfile() {
        assertThrows(CustomerBusinessException.class, () ->
                context.validate(CustomerType.PERSONAL, CustomerProfile.PYME));
    }

    @Test
    void shouldValidateBusinessStandardProfile() {
        assertDoesNotThrow(() ->
                context.validate(CustomerType.BUSINESS, CustomerProfile.STANDARD));
    }

    @Test
    void shouldValidateBusinessPymeProfile() {
        assertDoesNotThrow(() ->
                context.validate(CustomerType.BUSINESS, CustomerProfile.PYME));
    }

    @Test
    void shouldThrowWhenBusinessHasVipProfile() {
        assertThrows(CustomerBusinessException.class, () ->
                context.validate(CustomerType.BUSINESS, CustomerProfile.VIP));
    }

    @Test
    void shouldUseStandardWhenProfileIsNull() {
        assertDoesNotThrow(() ->
                context.validate(CustomerType.PERSONAL, null));
    }
}
