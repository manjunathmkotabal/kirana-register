package com.example.kirana.controllers;

import com.example.kirana.schemas.RecordTransactionRequest;
import com.example.kirana.entities.Transaction;
import com.example.kirana.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/record")
    public Transaction recordTransaction(@Validated @RequestBody RecordTransactionRequest request) {
        // Record the transaction
        return transactionService.recordTransaction(request);
    }

    @GetMapping("/fetch")
    public List<Transaction> fetchTransactions(@RequestParam(required = false) LocalDate startDate,
                                               @RequestParam(required = false) LocalDate endDate) {
        // You can use startDate and endDate directly or create a FetchTransactionsRequest object
        // and set these parameters based on business logic
        // Perform fetching operations using transactionService
        if (startDate != null && endDate != null) {
            // Fetch transactions based on the date range
            return transactionService.fetchTransactionsByDateRange(startDate, endDate);
        } else {
            // Fetch all transactions or apply default logic
            return transactionService.fetchAllTransactions();
        }
    }

    // Other endpoints for fetching, grouping, etc.
}