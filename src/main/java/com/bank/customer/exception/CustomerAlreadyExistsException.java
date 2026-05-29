package com.bank.customer.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String documentNumber) {
        super("Customer already exists with document number: " + documentNumber);
    }
}