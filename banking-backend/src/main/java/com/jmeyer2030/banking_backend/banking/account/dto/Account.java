package com.jmeyer2030.banking_backend.banking.account.dto;

import com.jmeyer2030.banking_backend.user.dto.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;


@Entity
@Table(name = "accounts")
public class Account {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "creation_date", nullable = false)
    private OffsetDateTime creationDate;

    public Account() {}

    public Account(Long id, Long balance, AccountType accountType, OffsetDateTime creationDate, User user) {
        this.id = id;
        this.balance = balance;
        this.accountType = accountType;
        this.creationDate = creationDate;
        this.user = user;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBalance() { return balance; }
    public void setBalance(Long balance) { this.balance = balance; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public OffsetDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(OffsetDateTime creationDate) { this.creationDate = creationDate; }
}
