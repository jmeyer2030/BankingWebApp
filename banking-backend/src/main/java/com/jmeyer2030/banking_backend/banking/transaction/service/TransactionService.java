package com.jmeyer2030.banking_backend.banking.transaction.service;

import com.jmeyer2030.banking_backend.authentication.JwtTokenProvider;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionForm;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionType;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;

public class TransactionService {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(JwtTokenProvider tokenProvider, UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Processes a transaction by validating it, updating balances, and adding the transaction to the database
     */
    public ResponseEntity<?> processTransaction(String token, TransactionForm transactionForm) {
        // Validate token
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.badRequest().body("Invalid session token.");
        }

        // Get username and id for the sender
        String fromUsername = tokenProvider.extractUsername(token);
        Long fromUserId = userRepository.findByUsername(fromUsername).getId();
        String toUsername = transactionForm.getRecipientUsername();

        // Validate recipient exists
        if (transactionForm.getType() == TransactionType.TRANSFER) { // If transfer
            if (!userRepository.existsByUsername(transactionForm.getRecipientUsername()) || // if user/account of recipient doesn't exist
                    !accountRepository.existsByUsername(transactionForm.getRecipientUsername())) {
                return ResponseEntity.badRequest().body("Recipient doesn't exist!");
            }
        }

        // Validate balance
        Account account = accountRepository.findByUserId(fromUserId).get();
        if (account.getBalance() < transactionForm.getAmount() && transactionForm.getType() != TransactionType.DEPOSIT) {
            return ResponseEntity.badRequest().body("Insufficient balance for this transaction!");
        }

        // Update balances
        long amount = transactionForm.getAmount();

        switch (transactionForm.getType()) {
            case DEPOSIT:
                updateBalanceDeposit(amount, fromUserId);
            case WITHDRAW:
                updateBalanceWithdraw(amount, fromUserId);
            case TRANSFER:
                String toUsername = account.getUsername();
                Long toUserId = userRepository.findByUsername(toUsername).getId();
                updateBalanceTransfer(amount, fromUserId, toUserId);
        }

        // Add transaction to db
        Transaction transaction = new Transaction(
                transactionForm.getAmount(),
                fromUserId,
                );


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
