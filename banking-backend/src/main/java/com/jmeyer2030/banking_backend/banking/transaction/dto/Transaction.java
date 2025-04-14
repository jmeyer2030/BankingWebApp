package com.jmeyer2030.banking_backend.banking.transaction.dto;

import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import com.jmeyer2030.banking_backend.user.dto.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Account senderAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Account recipientAccount;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @Column(name = "description")
    private String description;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;

    public Transaction(Long amount, TransactionType transactionType, String description, OffsetDateTime timestamp, Account senderAccount, Account recipientAccount) {
        this.amount = amount;
        this.type = transactionType;
        this.description = description;
        this.timestamp = timestamp;
        this.senderAccount = senderAccount;
        this.recipientAccount = recipientAccount;
    }

    public Transaction() {}


    public Account getSenderAccount() { return senderAccount; }
    public void setSenderAccount(Account senderAccount) { this.senderAccount = senderAccount; }

    public Account getRecipientAccount() { return recipientAccount; }
    public void setRecipientAccount(Account recipientAccount) { this.recipientAccount = recipientAccount; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
}
