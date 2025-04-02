package com.jmeyer2030.banking_backend.banking.account.service;

import com.jmeyer2030.banking_backend.authentication.JwtTokenProvider;
import com.jmeyer2030.banking_backend.authentication.service.AuthService;
import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.banking.account.dto.AccountResponse;
import com.jmeyer2030.banking_backend.banking.account.repository.AccountRepository;
import com.jmeyer2030.banking_backend.banking.transaction.repository.TransactionRepository;
import com.jmeyer2030.banking_backend.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthService authService, UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity<?> getAccountInformation(String token) {
        // If token is invalid
        if(!tokenProvider.validateToken(token)) {
            return ResponseEntity.badRequest().body("Invalid token");
        }


        String username = tokenProvider.extractUsername(token);

        // Get account/ check if exists
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Account doesn't exist");
        }

        Account account = accountOpt.get();
        Long userId = account.getUserId();
        Long accountId = account.getId();
        AccountResponse response = new AccountResponse();
        response.setAccountType(account.getAccountType().toString());
        response.setBalance(account.getBalance());
        response.setUsername(username);
        response.setAccountId(accountId);
        response.setRecentTransactions(transactionRepository.findTop10RecentTransactionsByUserId(userId, PageRequest.of(0, 10)));

        return ResponseEntity.ok(response);
    }

}
