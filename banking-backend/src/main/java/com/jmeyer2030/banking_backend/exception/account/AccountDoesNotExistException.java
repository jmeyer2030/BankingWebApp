package com.jmeyer2030.banking_backend.exception.account;

import com.jmeyer2030.banking_backend.banking.account.dto.Account;

public class AccountDoesNotExistException extends RuntimeException {
    public AccountDoesNotExistException(String message) {
        super(message);
    }

    public AccountDoesNotExistException() {
        super("Requested account does not exist!");
    }
}
