package com.example.kirana.entities;


import com.example.kirana.constants.enums.Currency;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amountINR;
    private  BigDecimal amountUSD;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getAmountINR() {
        return amountINR;
    }
    public BigDecimal getAmountUSD() {
        return amountUSD;
    }

    public void setAmountINR(BigDecimal amount) {
        this.amountINR = amount;
    }
    public void setAmountUSD(BigDecimal amount) {
        this.amountUSD = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    // Constructors, getters, and setters...
}
