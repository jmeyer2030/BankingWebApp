package com.jmeyer2030.banking_backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedList;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(new String[] {e.getMessage()});
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(new String[] {e.getMessage()});
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> UserDoesNotExistException(UserDoesNotExistException e) {
        return ResponseEntity.badRequest().body(new String[] {e.getMessage()});
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> IncorrectPasswordException(IncorrectPasswordException e) {
        return ResponseEntity.badRequest().body(new String[] {e.getMessage()});
    }
}
