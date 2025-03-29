package com.jmeyer2030.banking_backend.exception;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super("Username is incorrect");
    }


    public UserDoesNotExistException(String message) {
        super(message);
    }
}
