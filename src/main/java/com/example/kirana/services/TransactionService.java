package com.example.kirana.services;

        import com.example.kirana.constants.enums.Currency;
        import com.example.kirana.entities.Transaction;
        import com.example.kirana.services.FxRatesAPIService;
        import com.example.kirana.repository.TransactionRepository;
        import com.example.kirana.schemas.RecordTransactionRequest;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.math.BigDecimal;
        import java.math.RoundingMode;
        import java.time.LocalDate;
        import java.time.LocalDateTime;
        import java.time.LocalTime;
        import java.util.List;
        import java.util.Map;

@Service
public class TransactionService implements TransactionServiceInterface {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction recordTransaction(RecordTransactionRequest request) {
        // Implement logic to handle recording transactions
        BigDecimal amount = request.getAmount();
        Currency currency = request.getCurrency();

        Transaction transaction = new Transaction();
        transaction.setCurrency(currency);
        transaction.setTransactionDate(LocalDateTime.now());

        Map<Currency, BigDecimal> exchangeRates = FxRatesAPIService.fetchExchangeRates();
        if (currency != Currency.INR && currency != Currency.USD) {

            // Convert amount to INR and USD based on the fetched rates
            BigDecimal amountInINR = calculateAmountInINR(amount, currency, exchangeRates);
            BigDecimal amountInUSD = calculateAmountInUSD(amount, currency, exchangeRates);

            // Store the amount in INR and USD columns
            transaction.setAmountINR(amountInINR);
            transaction.setAmountUSD(amountInUSD);
        } else if (currency == Currency.INR) {
            transaction.setAmountINR(amount);
//            transaction.setAmountUSD(calculateAmountInUSD(amount, Currency.INR,exchangeRates)); // Same amount in USD
        } else {
            transaction.setAmountINR(calculateAmountInINR(amount, Currency.USD, exchangeRates));
            transaction.setAmountUSD(amount); // Same amount in INR
        }

        // Record the transaction with amounts in respective columns
        return transactionRepository.save(transaction);
    }

    private BigDecimal calculateAmountInINR(BigDecimal amount, Currency currency, Map<Currency, BigDecimal> exchangeRates) {
        BigDecimal exchangeRate = exchangeRates.get(currency);
        return amount.multiply(exchangeRate);
    }

    private BigDecimal calculateAmountInUSD(BigDecimal amount, Currency currency, Map<Currency, BigDecimal> exchangeRates) {
        BigDecimal exchangeRateToUSD = exchangeRates.get(currency).divide(exchangeRates.get(Currency.USD), 6, RoundingMode.HALF_UP);
        return amount.multiply(exchangeRateToUSD);
    }
    public List<Transaction> fetchTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        // Implement logic to fetch transactions within the specified date range
        // Use transactionRepository or other mechanisms

        // Example:
        return transactionRepository.findByTransactionDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
    }

    public List<Transaction> fetchAllTransactions() {
        // Implement logic to fetch all transactions
        // Use transactionRepository or other mechanisms
        return transactionRepository.findAll();
    }

    // Implement other service methods for fetching, grouping, etc.
}
