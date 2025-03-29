package com.jmeyer2030.banking_backend.banking.dto;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class Transaction {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String description;
}
