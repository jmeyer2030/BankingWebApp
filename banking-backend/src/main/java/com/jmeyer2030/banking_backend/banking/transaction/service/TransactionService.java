package com.jmeyer2030.banking_backend.banking.transaction.service;

import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionDetails;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionForm;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionType;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.exception.account.AccountDoesNotExistException;
import com.jmeyer2030.banking_backend.exception.authentication.InvalidTokenException;
import com.jmeyer2030.banking_backend.exception.transaction.InsufficientFundsException;
import com.jmeyer2030.banking_backend.exception.transaction.InvalidRecipientException;
import com.jmeyer2030.banking_backend.payload.ApiResponse;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
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
    * Processes a request and generates a response for a page of transaction history
    */
    public ResponseEntity<?> getTransactionPage(String token, Pageable pageable, Long accountId) {
        // Verify token
        Long userId = authService.verifyTokenAndGetUserId(token);

        // Verify account exists
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new AccountDoesNotExistException("The requested account does not exist");
        }
        Account account = accountOpt.get();

        // Verify account belongs to the user
        if (!Objects.equals(account.getUser().getId(), userId)) {
            throw new InvalidTokenException("You do not have access to view this account");
        }

        Page<Transaction> transactionPage = transactionRepository.findTransactionsByUserId(accountId, pageable);

        Page<TransactionDetails> transactionDetailsPage= transactionPage.map(this::getDetailsFromTransaction);

        return ResponseEntity.ok().body(new ApiResponse(
            true,
            "Request successful",
            transactionDetailsPage
        ));
    }



    /**
     * Processes a transaction by validating it, updating balances, and adding the transaction to the database
     */
    @Transactional
    public ResponseEntity<?> processTransaction(String token, TransactionForm transactionForm) {
        // Authenticate token and get userId
        Long fromUserId = authService.verifyTokenAndGetUserId(token);

        // If Transfer, we need to validate/get the recipient's information
        Long toUserId = null;
        Account toAccount = null;
        if (transactionForm.getTransactionType() == TransactionType.TRANSFER) {
            toAccount = getValidAccountByUsername(transactionForm.getRecipientUsername());
            toUserId = toAccount.getUser().getId();

            if (toUserId.equals(fromUserId)) {
                throw new InvalidRecipientException("Cannot transfer funds to the same account.");
            }
        }

        // Get from account
        Account fromAccount = getValidAccountByUserId(fromUserId);

        // If insufficient funds
        if (transactionForm.getTransactionType() != TransactionType.DEPOSIT && fromAccount.getBalance() < transactionForm.getAmount()) {
            throw new InsufficientFundsException();
        }

        // Update balances
        updateAccountBalances(fromAccount, toAccount, transactionForm.getTransactionType(), transactionForm.getAmount());

        // Save new transaction to DB
        transactionRepository.save(
                new Transaction(
                        transactionForm.getAmount(),
                        transactionForm.getTransactionType(),
                        transactionForm.getDescription(),
                        OffsetDateTime.now(),
                        fromAccount,
                        toAccount
                ));

        return ResponseEntity.ok().body("Transaction processed!");
    }

    public List<TransactionDetails> getTransactionDetails(Long accountId, Pageable pageable) {
        List<Transaction> transactionList = transactionRepository.findTransactionsByUserIdList(accountId, pageable);

        List<TransactionDetails> result = new LinkedList<>();

        for (Transaction transaction : transactionList) {
            result.add(getDetailsFromTransaction(transaction));
        }

        return result;
    }

    private TransactionDetails getDetailsFromTransaction(Transaction transaction) {
        String[] transactionTypeToString = new String[] {"Transfer", "Deposit", "Withdraw"};
        return new  TransactionDetails(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getSenderAccount().getId(),
                transaction.getSenderAccount().getUser().getUsername(),
                transaction.getType() == TransactionType.TRANSFER ? transaction.getRecipientAccount().getId() : null,
                transaction.getType() == TransactionType.TRANSFER ? transaction.getRecipientAccount().getUser().getUsername() : null,
                transactionTypeToString[transaction.getType().ordinal()],
                transaction.getDescription(),
                transaction.getTimestamp()
        );
    }

    private Account getValidAccountByUsername(String username) {
        // Check semantics of username
        if (username == null || username.isBlank()) {
            throw new AccountDoesNotExistException("The requested account's name must not be empty.");
        }

        Optional<Account> accountOptional = accountRepository.findByUser_Username(username);

        if (accountOptional.isEmpty()) {
            throw new AccountDoesNotExistException("The requested account, " + username + ", does not exist.");
        }

        return accountOptional.get();
    }

    private Account getValidAccountByUserId(Long userId) {
        // Check semantics of username
        if (userId == null) {
            throw new AccountDoesNotExistException("The requested account's ID must not be null.");
        }

        Optional<Account> accountOptional = accountRepository.findByUser_Id(userId);

        if (accountOptional.isEmpty()) {
            throw new AccountDoesNotExistException("The requested account with id, " + userId  + ", does not exist.");
        }

        return accountOptional.get();
    }

    private void updateAccountBalances(Account fromAccount, Account toAccount, TransactionType transactionType, long amount) {
        switch (transactionType) {
            case DEPOSIT:
                updateBalanceDeposit(fromAccount, amount);
                break;
            case WITHDRAW:
                updateBalanceWithdraw(fromAccount, amount);
                break;
            case TRANSFER:
                updateBalanceTransfer(fromAccount, toAccount, amount);
                break;
        }

    }

    /**
     * Subtract amount from "fromAccount", add amount to "toAccount"
     */
    private void updateBalanceTransfer(Account fromAccount, Account toAccount, Long amount) {
        accountRepository.adjustBalance(fromAccount.getId(), -amount);
        accountRepository.adjustBalance(toAccount.getId(), amount);
    }

    /**
     * Add amount to account balance
     */
    private void updateBalanceDeposit(Account account, Long amount) {
        accountRepository.adjustBalance(account.getId(), amount);
    }

    /**
     * Subtract amount from account balance
     */
    private void updateBalanceWithdraw(Account account, Long amount) {
        accountRepository.adjustBalance(account.getId(), -amount);
    }
}
