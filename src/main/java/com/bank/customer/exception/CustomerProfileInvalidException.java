package com.bank.customer.exception;

public class CustomerProfileInvalidException extends RuntimeException {
    public CustomerProfileInvalidException(String message) {
        super(message);
    }
}
