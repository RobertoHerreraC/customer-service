package com.bank.customer.exception;

public class CustomerBusinessException extends RuntimeException {
    public CustomerBusinessException(String message) {
        super(message);
    }
}
