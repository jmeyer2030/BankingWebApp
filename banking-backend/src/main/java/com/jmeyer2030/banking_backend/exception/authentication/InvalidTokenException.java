package com.jmeyer2030.banking_backend.exception.authentication;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException() {
        super("Session expired! Please login again.");
    }
}
