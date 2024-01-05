package com.example.kirana.controllers;

import com.example.kirana.schemas.RecordTransactionRequest;
import com.example.kirana.entities.Transaction;
import com.example.kirana.schemas.TransactionOrReport;
import com.example.kirana.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

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
    public ResponseEntity<?> fetchTransactions(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "group", required = false, defaultValue = "false") boolean group
    ) {
        TransactionOrReport result = new TransactionOrReport();

        if (date != null && group) {
            // Fetch and group transactions by date for daily reports
            result.setReports(transactionService.fetchAndGroupTransactionsByDate(date));
        } else if (date != null) {
            // Fetch transactions for the specified date
            result.setTransactions(transactionService.fetchTransactionsByDate(date));
        } else {
            // Fetch all transactions
            result.setTransactions(transactionService.fetchAllTransactions());
        }

        return ResponseEntity.ok().body(result);
    }



    // Other endpoints for fetching, grouping, etc.
}