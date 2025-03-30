package com.jmeyer2030.banking_backend.exception;

public class LoginExpiredException extends RuntimeException {
    public LoginExpiredException(String message) {
        super(message);
    }

    public LoginExpiredException() {
        super("Session expired! Please login again.");
    }
}
