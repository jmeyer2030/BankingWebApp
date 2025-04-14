package com.jmeyer2030.banking_backend.exception;

import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.exception.account.AccountDoesNotExistException;
import com.jmeyer2030.banking_backend.exception.authentication.InvalidTokenException;
import com.jmeyer2030.banking_backend.exception.login.IncorrectLoginCredentialsException;
import com.jmeyer2030.banking_backend.exception.registration.EmailAlreadyExistsException;
import com.jmeyer2030.banking_backend.exception.registration.UsernameAlreadyExistsException;
import com.jmeyer2030.banking_backend.exception.transaction.InsufficientFundsException;
import com.jmeyer2030.banking_backend.exception.transaction.InvalidRecipientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    // Registration
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // Login
    @ExceptionHandler(IncorrectLoginCredentialsException.class)
    public ResponseEntity<?> handleIncorrectLoginCredentialsException(IncorrectLoginCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // Transaction
    @ExceptionHandler(InvalidRecipientException.class)
    public ResponseEntity<?> handleInvalidRecipientException(InvalidRecipientException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<?> handleInsufficientFundsException(InsufficientFundsException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    // Authentication
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // Account
    @ExceptionHandler(AccountDoesNotExistException.class)
    public ResponseEntity<?> handleAccountDoesNotExistException(AccountDoesNotExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
