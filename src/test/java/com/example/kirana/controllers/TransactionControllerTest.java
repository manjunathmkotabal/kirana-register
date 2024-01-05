package com.example.kirana.controllers;

import com.example.kirana.controllers.TransactionController;
import com.example.kirana.entities.Transaction;
import com.example.kirana.schemas.DailyTransactionReport;
import com.example.kirana.schemas.TransactionOrReport;
import com.example.kirana.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    private final TransactionService transactionService = mock(TransactionService.class);
    private final TransactionController controller = new TransactionController(transactionService);

    @Test
    void fetchTransactionsBySingleDate() {
        LocalDate date = LocalDate.of(2024, 1, 5);
        when(transactionService.fetchTransactionsByDate(date)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = controller.fetchTransactions(date, null, null, false);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        TransactionOrReport result = (TransactionOrReport) response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getTransactions()).isEmpty();
        verify(transactionService, times(1)).fetchTransactionsByDate(date);
    }

    @Test
    void fetchTransactionsByDateRangeWithoutGrouping() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);
        when(transactionService.fetchTransactionsByDateRange(startDate, endDate)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = controller.fetchTransactions(null, startDate, endDate, false);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        TransactionOrReport result = (TransactionOrReport) response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getTransactions()).isEmpty();
        verify(transactionService, times(1)).fetchTransactionsByDateRange(startDate, endDate);
    }

    @Test
    void fetchTransactionsByDateRangeWithGrouping() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);
        when(transactionService.fetchAndGroupTransactionsByDateRange(startDate, endDate))
                .thenReturn(Collections.singletonList(new DailyTransactionReport(startDate, Collections.emptyList())));

        ResponseEntity<?> response = controller.fetchTransactions(null, startDate, endDate, true);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        TransactionOrReport result = (TransactionOrReport) response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getReports()).hasSize(1);
        verify(transactionService, times(1)).fetchAndGroupTransactionsByDateRange(startDate, endDate);
    }

    @Test
    void fetchAllTransactions() {
        when(transactionService.fetchAllTransactions()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = controller.fetchTransactions(null, null, null, false);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        TransactionOrReport result = (TransactionOrReport) response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getTransactions()).isEmpty();
        verify(transactionService, times(1)).fetchAllTransactions();
    }
}
