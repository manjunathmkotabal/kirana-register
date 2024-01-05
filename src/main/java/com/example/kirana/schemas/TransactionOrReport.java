package com.example.kirana.schemas;

import com.example.kirana.entities.Transaction;

import java.util.List;

public class TransactionOrReport {
    private List<Transaction> transactions;
    private List<DailyTransactionReport> reports;

    // constructor, getters, and setters

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<DailyTransactionReport> getReports() {
        return reports;
    }

    public void setReports(List<DailyTransactionReport> reports) {
        this.reports = reports;
    }
}
