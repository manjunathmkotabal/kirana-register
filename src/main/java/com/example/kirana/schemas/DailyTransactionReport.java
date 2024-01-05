package com.example.kirana.schemas;

import com.example.kirana.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DailyTransactionReport {
    private LocalDate date;
    private List<Transaction> transactions;
    private BigDecimal totalAmountINR;
    private BigDecimal totalAmountUSD;

    public DailyTransactionReport(LocalDate date, List<Transaction> transactions) {
        this.date = date;
        this.transactions = transactions;
        this.totalAmountINR = calculateTotalAmountINR(transactions);
        this.totalAmountUSD = calculateTotalAmountUSD(transactions);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getTotalAmountINR() {
        return totalAmountINR;
    }

    public void setTotalAmountINR(BigDecimal totalAmountINR) {
        this.totalAmountINR = totalAmountINR;
    }

    public BigDecimal getTotalAmountUSD() {
        return totalAmountUSD;
    }

    public void setTotalAmountUSD(BigDecimal totalAmountUSD) {
        this.totalAmountUSD = totalAmountUSD;
    }

    private BigDecimal calculateTotalAmountINR(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getAmountINR)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalAmountUSD(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getAmountUSD)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and setters for date, transactions, totalAmountINR, and totalAmountUSD
}
