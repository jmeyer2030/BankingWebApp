package com.jmeyer2030.banking_backend.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException() {
        super("Not enough funds for this transaction!");
    }
}
