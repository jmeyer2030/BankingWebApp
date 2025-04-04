package com.jmeyer2030.banking_backend.exception.login;

public class IncorrectLoginCredentialsException extends RuntimeException {
    public IncorrectLoginCredentialsException(String message) {
        super(message);
    }

    public IncorrectLoginCredentialsException() {
        super("Username or password is incorrect");
    }
}
