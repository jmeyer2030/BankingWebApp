package com.jmeyer2030.banking_backend.exception.registration;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException() {
        super("Another account with that email already exists");
    }
}
