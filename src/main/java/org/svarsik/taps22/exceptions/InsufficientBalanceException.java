package org.svarsik.taps22.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("User balance is not enough");
    }
}