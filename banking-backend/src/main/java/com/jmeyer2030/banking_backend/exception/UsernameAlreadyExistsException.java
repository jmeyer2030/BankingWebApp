package com.jmeyer2030.banking_backend.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameAlreadyExistsException() {
        super("Another account with that username already exists");
    }
}
