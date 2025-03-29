package com.jmeyer2030.banking_backend.exception;

import jakarta.persistence.criteria.CriteriaBuilder;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Passoword is incorrect");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
