package com.jmeyer2030.banking_backend.banking.account.dto;

import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;
import com.jmeyer2030.banking_backend.banking.transaction.dto.TransactionDetails;

import java.util.List;

public class AccountResponse {
    private AccountType accountType;
    private Long userId;
    private String username;
    private Long accountId;
    private Long balance;
    private List<TransactionDetails> recentTransactions;

    public AccountResponse(Long accountId, AccountType accountType, String username, Long balance, List<TransactionDetails> recentTransactions, Long userId) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.username = username;
        this.balance = balance;
        this.recentTransactions = recentTransactions;
        this.userId = userId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getBalance() { return balance; }
    public void setBalance(Long balance) { this.balance = balance; }

    public List<TransactionDetails> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<TransactionDetails> recentTransactions) { this.recentTransactions = recentTransactions; }
}
