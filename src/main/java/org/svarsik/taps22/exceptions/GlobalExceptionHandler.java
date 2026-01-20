package org.svarsik.taps22.exceptions;

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
