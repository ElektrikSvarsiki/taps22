package org.svarsik.taps22.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> insufficient() {
        return Map.of(
                "errorCode", "INSUFFICIENT_BALANCE",
                "message", "User balance is not enough"
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> userNotFound() {
        return Map.of(
                "errorCode", "USER_NOT_FOUND",
                "message", "User not found"
        );
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> paymentNotFound() {
        return Map.of(
                "errorCode", "PAYMENT_NOT_FOUND",
                "message", "Payment not found"
        );
    }
}
