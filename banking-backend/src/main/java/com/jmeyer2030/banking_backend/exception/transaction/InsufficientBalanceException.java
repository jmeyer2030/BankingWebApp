package com.jmeyer2030.banking_backend.exception.transaction;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException() {
        super("Insufficient balance to complete this transaction.");
    }
}
