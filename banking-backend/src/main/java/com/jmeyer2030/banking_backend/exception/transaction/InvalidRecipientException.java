package com.jmeyer2030.banking_backend.exception.transaction;

public class InvalidRecipientException extends RuntimeException {
    public InvalidRecipientException(String message) {
        super(message);
    }

    public InvalidRecipientException() {
        super("Transfer failed because the recipient doesn't exist.");
    }
}
