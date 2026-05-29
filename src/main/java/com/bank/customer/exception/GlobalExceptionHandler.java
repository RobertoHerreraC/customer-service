package com.bank.customer.exception;

import com.bank.customer.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.OffsetDateTime;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExists(CustomerAlreadyExistsException exception) {
        ErrorResponse error = new ErrorResponse();
        error.setCode("CUSTOMER_ALREADY_EXISTS");
        error.setMessage(exception.getMessage());
        error.setTimestamp(Date.from(OffsetDateTime.now().toInstant()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(
            CustomerNotFoundException exception,
            ServerWebExchange exchange) {

        ErrorResponse error = new ErrorResponse();
        error.setCode("CUSTOMER_NOT_FOUND");
        error.setMessage(exception.getMessage());
        error.setPath(exchange.getRequest().getPath().value());
        error.setTimestamp(Date.from(OffsetDateTime.now().toInstant()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}