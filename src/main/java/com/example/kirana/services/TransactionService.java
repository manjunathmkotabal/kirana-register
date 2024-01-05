package com.example.kirana.services;

        import com.example.kirana.constants.enums.Currency;
        import com.example.kirana.entities.Transaction;
        import com.example.kirana.schemas.DailyTransactionReport;
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
        import java.util.stream.Collectors;

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
            transaction.setAmountUSD(calculateAmountInUSD(amount, Currency.INR, exchangeRates)); // Same amount in USD
        } else {
            transaction.setAmountINR(calculateAmountInINR(amount, Currency.INR, exchangeRates));
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
        BigDecimal exchangeRate = exchangeRates.get(currency);
        return amount.divide(exchangeRate, 6, RoundingMode.HALF_UP);
    }

    public List<Transaction> fetchAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> fetchTransactionsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return transactionRepository.findByTransactionDateBetween(startOfDay, endOfDay);
    }

    // Fetch transactions and group them by date for daily reports
    public List<DailyTransactionReport> fetchAndGroupTransactionsByDate(LocalDate date) {
        List<Transaction> transactions = fetchTransactionsByDate(date);

        Map<LocalDate, List<Transaction>> groupedTransactions = transactions.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getTransactionDate().toLocalDate()));

        return groupedTransactions.entrySet().stream()
                .map(entry -> new DailyTransactionReport(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }



    // Implement other service methods for fetching, grouping, etc.
}
