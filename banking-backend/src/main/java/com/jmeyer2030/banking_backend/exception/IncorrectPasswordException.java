package com.jmeyer2030.banking_backend.exception;

import jakarta.persistence.criteria.CriteriaBuilder;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Password is incorrect");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
