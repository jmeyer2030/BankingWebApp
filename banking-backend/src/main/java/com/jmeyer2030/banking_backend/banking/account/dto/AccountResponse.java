package com.jmeyer2030.banking_backend.banking.account.dto;

import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;

import java.util.List;

public class AccountResponse {
    private Long accountId;
    private String accountType;
    private String username;
    private double balance;
    private List<Transaction> recentTransactions;

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public List<Transaction> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<Transaction> recentTransactions) { this.recentTransactions = recentTransactions; }
}
