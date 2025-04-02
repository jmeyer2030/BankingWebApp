package com.jmeyer2030.banking_backend.banking.transaction.controller;

import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionForm;
import com.jmeyer2030.banking_backend.banking.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transact")
    ResponseEntity<?> processTransaction(@CookieValue(name = "accountToken", required = false) String token,
                                         @Valid @RequestBody TransactionForm transactionForm,
                                         BindingResult result) {
        // Check for form errors
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        return transactionService.processTransaction(token, transactionForm);
    }
}
