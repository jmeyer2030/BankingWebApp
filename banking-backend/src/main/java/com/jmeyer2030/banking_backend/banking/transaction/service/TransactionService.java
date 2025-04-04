package com.jmeyer2030.banking_backend.banking.transaction.service;

import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionForm;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionType;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.exception.transaction.InsufficientFundsException;
import com.jmeyer2030.banking_backend.exception.transaction.InvalidRecipientException;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

public class TransactionService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AuthService authService, UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Processes a transaction by validating it, updating balances, and adding the transaction to the database
     */
    public ResponseEntity<?> processTransaction(String token, TransactionForm transactionForm) {
        // Authenticate token and get userId
        Long fromUserId = authService.authenticateAndGetUserId(token);

        // If Transfer, we need to validate/get the recipient's information
        Long toUserId = null;
        Long toAccountId = null;
        if (transactionForm.getType() == TransactionType.TRANSFER) {
            toUserId = validateAndGetRecipientId(transactionForm.getRecipientUsername());
            toAccountId = validateAndGetRecipientAccountId(transactionForm.getRecipientUsername());
        }

        // This should never happen, since an account is created on register
        Account fromAccount = accountRepository.findByUserId(fromUserId)
                .orElseThrow(() -> new IllegalStateException("Account not found for user: " + fromUserId));

        // If insufficient funds
        if (transactionForm.getType() != TransactionType.DEPOSIT && fromAccount.getBalance() < transactionForm.getAmount()) {
            throw new InsufficientFundsException();
        }

        // Update balances
        updateAccountBalances(transactionForm.getType(), transactionForm.getAmount(), fromAccount.getId(), toAccountId);

        // Save new transaction to DB
        transactionRepository.save(
                new Transaction(
                        transactionForm.getAmount(),
                        fromUserId,
                        toUserId,
                        transactionForm.getType(),
                        transactionForm.getDescription(),
                        OffsetDateTime.now()
                ));

        return ResponseEntity.ok().body("Transaction processed!");
    }


    public Long validateAndGetRecipientId(String recipientUsername) {
        // Check semantics of recipientUsername
        if (recipientUsername == null || recipientUsername.isBlank()) {
            throw new InvalidRecipientException();
        }

        // Check that they have an account and a username
        if (!userRepository.existsByUsername(recipientUsername)) {
            throw new InvalidRecipientException();
        }

        return userRepository.findByUsername(recipientUsername).getId();
    }

    public Long validateAndGetRecipientAccountId(String recipientUsername) {
        // Check semantics of recipientUsername
        if (recipientUsername == null || recipientUsername.isBlank()) {
            throw new InvalidRecipientException();
        }

        Optional<Account> accountOptional = accountRepository.findByUsername(recipientUsername);

        if (accountOptional.isEmpty()) {
            throw new InvalidRecipientException();
        }

        return accountOptional.get().getId();
    }

    private void updateAccountBalances(TransactionType transactionType, long amount, Long fromAccountId, Long toAccountId) {
        switch (transactionType) {
            case DEPOSIT:
                updateBalanceDeposit(amount, fromAccountId);
            case WITHDRAW:
                updateBalanceWithdraw(amount, fromAccountId);
            case TRANSFER:
                updateBalanceTransfer(amount, fromAccountId, toAccountId);
        }

    }

    /**
     * Subtract amount from "fromAccount", add amount to "toAccount"
     */
    public void updateBalanceTransfer(Long amount, Long fromAccountId, Long toAccountId) {
        Long fromInitialBalance = accountRepository.findById(fromAccountId).get().getBalance();
        Long toInitialBalance = accountRepository.findById(toAccountId).get().getBalance();
        accountRepository.updateBalance(fromAccountId, fromInitialBalance - amount);
        accountRepository.updateBalance(toAccountId, toInitialBalance + amount);
    }

    /**
     * Add amount to account balance
     */
    public void updateBalanceDeposit(Long amount, Long accountId) {
        Long initialBalance = accountRepository.findById(accountId).get().getBalance();
        accountRepository.updateBalance(accountId, initialBalance + amount);
    }

    /**
     * Subtract amount from account balance
     */
    public void updateBalanceWithdraw(Long amount, Long accountId) {
        Long initialBalance = accountRepository.findById(accountId).get().getBalance();
        accountRepository.updateBalance(accountId, initialBalance - amount);
    }

}
