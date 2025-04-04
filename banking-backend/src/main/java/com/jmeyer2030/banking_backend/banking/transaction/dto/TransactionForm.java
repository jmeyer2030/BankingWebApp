package com.jmeyer2030.banking_backend.banking.transaction.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
/**
* Represents the data that the user sends in the form of a form
* Requires:
*  - amount
*  - type (transfer, deposit, withdrawal)
*  -
*/
public class TransactionForm {
    @NotBlank(message = "Amount is required")
    @Min(value = 1, message = "Transaction amount must be >$0.01!") // Value is inclusive
    private Long amount;

    @NotNull(message = "Type is required") // TRANSFER, DEPOSIT, or WITHDRAW
    private TransactionType type;

    @NotBlank(message = "Description is required")
    private String description;

    private String recipientUsername;

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRecipientUsername() { return recipientUsername; };
    public void setRecipientUsername(String recipientUsername) { this.recipientUsername = recipientUsername; }
}
