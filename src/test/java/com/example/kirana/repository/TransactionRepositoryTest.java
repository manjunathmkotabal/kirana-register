package com.example.kirana.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.kirana.entities.Transaction;
import com.example.kirana.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for TransactionRepository
 */
@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void testFindByTransactionDateBetween() {
        // Mocking the input data
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 23, 59, 59);

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setTransactionDate(startDate);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setTransactionDate(startDate);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        // Mocking the repository method
        when(transactionRepository.findByTransactionDateBetween(startDate, endDate))
                .thenReturn(transactions);

        // Calling the repository method
        List<Transaction> result = transactionRepository.findByTransactionDateBetween(startDate, endDate);

        // Verifying the result
        assertEquals(transactions.size(), result.size());
        assertEquals(transactions.get(0).getId(), result.get(0).getId());
        assertEquals(transactions.get(1).getId(), result.get(1).getId());

        // Verifying the method was called with the correct parameters
        verify(transactionRepository, times(1)).findByTransactionDateBetween(startDate, endDate);
    }
}
