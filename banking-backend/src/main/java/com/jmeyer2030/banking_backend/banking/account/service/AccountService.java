package com.jmeyer2030.banking_backend.banking.account.service;

import com.jmeyer2030.banking_backend.authentication.jwt.JwtTokenProvider;
import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.dto.AccountResponse;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.banking.transaction.service.TransactionService;
import com.jmeyer2030.banking_backend.exception.account.AccountDoesNotExistException;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthService authService, UserRepository userRepository, JwtTokenProvider tokenProvider, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    public ResponseEntity<AccountResponse> getAccountInformation(String token) {
        // Get userId/verify
        Long userId = authService.verifyTokenAndGetUserId(token);

        // Get account/ check if exists
        Optional<Account> accountOpt = accountRepository.findByUser_Id(userId);
        if (accountOpt.isEmpty()) {
            throw new AccountDoesNotExistException();
        }

        // If exists, generate and return account response
        Account account = accountOpt.get();
        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getAccountType(),
                account.getUser().getUsername(),
                account.getBalance(),
                transactionService.getTransactionDetails(account.getId(), PageRequest.of(0, 10)),
                userId
                );

        return ResponseEntity.ok(response);
    }


}
