package com.example.kirana.services;

import com.example.kirana.constants.enums.Currency;
import com.example.kirana.entities.Transaction;
import com.example.kirana.repository.TransactionRepository;
import com.example.kirana.schemas.DailyTransactionReport;
import com.example.kirana.schemas.RecordTransactionRequest;
import com.example.kirana.services.FxRatesAPIService;
import com.example.kirana.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for TransactionService
 */
@SpringBootTest
class TransactionServiceTest {

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private final FxRatesAPIService fxRatesAPIService = mock(FxRatesAPIService.class);
    private final TransactionService transactionService = new TransactionService(transactionRepository, fxRatesAPIService);

    @Test
    void recordTransaction() {
        RecordTransactionRequest request = new RecordTransactionRequest();
        request.setAmount(BigDecimal.TEN);
        request.setCurrency(Currency.INR);

        when(fxRatesAPIService.fetchExchangeRates()).thenReturn(Collections.singletonMap(Currency.INR, BigDecimal.ONE));

        Transaction savedTransaction = new Transaction();
        savedTransaction.setAmountINR(BigDecimal.TEN);
        savedTransaction.setAmountUSD(BigDecimal.TEN);

        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(savedTransaction);

        Transaction recordedTransaction = transactionService.recordTransaction(request);

        assertThat(recordedTransaction).isNotNull();
        assertThat(recordedTransaction.getAmountINR()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(recordedTransaction.getAmountUSD()).isEqualByComparingTo(BigDecimal.TEN);

        verify(transactionRepository, times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    void fetchAllTransactions() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));

        List<Transaction> fetchedTransactions = transactionService.fetchAllTransactions();

        assertThat(fetchedTransactions).isNotNull();
        assertThat(fetchedTransactions.size()).isEqualTo(1);
        assertThat(fetchedTransactions.get(0).getId()).isEqualTo(1L);

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void fetchTransactionsByDate() {
        LocalDateTime date = LocalDate.of(2023, 1, 1).atStartOfDay();
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(date);

        when(transactionRepository.findByTransactionDateBetween(date, date.withHour(23).withMinute(59).withSecond(59).withNano(999999999)))
                .thenReturn(Collections.singletonList(transaction));

        List<Transaction> fetchedTransactions = transactionService.fetchTransactionsByDate(LocalDate.of(2023, 1, 1));

        assertThat(fetchedTransactions).isNotNull();
        assertThat(fetchedTransactions.size()).isEqualTo(1);
        assertThat(fetchedTransactions.get(0).getTransactionDate()).isEqualTo(date);

        verify(transactionRepository, times(1)).findByTransactionDateBetween(date, date.withHour(23).withMinute(59).withSecond(59).withNano(999999999));
    }

    @Test
    void fetchTransactionsByDateRange() {
        LocalDateTime startDate = LocalDate.of(2023, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 1, 2).atTime(23, 59, 59, 999999999);
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(startDate);

        when(transactionRepository.findByTransactionDateBetween(startDate, endDate))
                .thenReturn(Collections.singletonList(transaction));

        List<Transaction> fetchedTransactions = transactionService.fetchTransactionsByDateRange(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2));

        assertThat(fetchedTransactions).isNotNull();
        assertThat(fetchedTransactions.size()).isEqualTo(1);
        assertThat(fetchedTransactions.get(0).getTransactionDate()).isEqualTo(startDate);

        verify(transactionRepository, times(1)).findByTransactionDateBetween(startDate, endDate);
    }


    @Test
    public void fetchAndGroupTransactionsByDateRangeTest() {
        LocalDateTime startDate = LocalDate.of(2023, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 1, 5).atStartOfDay();

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setTransactionDate(startDate);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setTransactionDate(endDate);

        when(transactionRepository.findByTransactionDateBetween(startDate, endDate))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        List<DailyTransactionReport> reports = transactionService.fetchAndGroupTransactionsByDateRange(startDate.toLocalDate(), endDate.toLocalDate());
        // Add assertions to verify the result
    }


    // Add more test cases for fetchAllTransactions, fetchTransactionsByDate, fetchAndGroupTransactionsByDate, fetchTransactionsByDateRange, fetchAndGroupTransactionsByDateRange
}
