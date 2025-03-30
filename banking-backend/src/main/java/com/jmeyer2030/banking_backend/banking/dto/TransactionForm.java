package com.jmeyer2030.banking_backend.banking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
/**
* Represents the data that the user sends in the form of a form
*
*/
public class TransactionForm {
    @NotBlank(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be >$0.01!") // Value is inclusive
    private BigDecimal amount;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Description is required")
    private String description;


    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
