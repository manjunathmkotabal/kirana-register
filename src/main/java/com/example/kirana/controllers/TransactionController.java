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

    /**
     * Endpoint to record a transaction based on the provided request.
     *
     * @param request The request object containing transaction details such as amount and Currency.
     * @return The recorded transaction.
     */
    @PostMapping("/record")
    public Transaction recordTransaction(@Validated @RequestBody RecordTransactionRequest request) {
        // Record the transaction
        return transactionService.recordTransaction(request);
    }

    /**
     * Endpoint to fetch transactions based on various parameters like date, date range, and grouping.
     *
     * @param date      Single date for fetching transactions.
     * @param startDate Start date for fetching transactions within a range.
     * @param endDate   End date for fetching transactions within a range.
     * @param group     Flag to enable grouping for daily reports.
     * @return Response containing transactions or reports based on the parameters provided.
     */
    @GetMapping("/fetch")
    public ResponseEntity<?> fetchTransactions(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "group", required = false, defaultValue = "false") boolean group
    ) {
        TransactionOrReport result = new TransactionOrReport();

        if (date != null) {
            if (group) {
                // Fetch and group transactions by single date for daily reports
                result.setReports(transactionService.fetchAndGroupTransactionsByDate(date));
            } else {
                // Fetch transactions for the specified date
                result.setTransactions(transactionService.fetchTransactionsByDate(date));
            }
        } else if (startDate != null && endDate != null) {
            if(group){
                // Fetch and group transactions by date range for daily reports
                result.setReports(transactionService.fetchAndGroupTransactionsByDateRange(startDate, endDate));
            }
            else {
                // Fetch transactions within the date range
                result.setTransactions(transactionService.fetchTransactionsByDateRange(startDate, endDate));
            }
        } else {
            // Fetch all transactions
            result.setTransactions(transactionService.fetchAllTransactions());
        }

        return ResponseEntity.ok().body(result);
    }

    // Other endpoints for fetching, grouping, etc.
}