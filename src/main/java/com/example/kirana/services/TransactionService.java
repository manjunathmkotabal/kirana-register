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


/**
 * Service responsible for handling transaction-related operations.
 */
@Service
public class TransactionService implements TransactionServiceInterface {
    private final TransactionRepository transactionRepository;
    private final FxRatesAPIService fxRatesAPIService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, FxRatesAPIService fxRatesAPIService) {
        this.transactionRepository = transactionRepository;
        this.fxRatesAPIService = fxRatesAPIService;
    }

    /**
     * Records a transaction based on the provided request data.
     * @param request The request containing transaction details.
     * @return The recorded transaction.
     */
    @Override
    public Transaction recordTransaction(RecordTransactionRequest request) {
        // Implement logic to handle recording transactions
        BigDecimal amount = request.getAmount();
        Currency currency = request.getCurrency();

        Transaction transaction = new Transaction();
        transaction.setCurrency(currency);
        transaction.setTransactionDate(LocalDateTime.now());

        Map<Currency, BigDecimal> exchangeRates = fxRatesAPIService.fetchExchangeRates();
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

    /**
     * Fetches transactions for a specific date.
     *
     * @param date The date for which transactions are fetched.
     * @return List of transactions for the provided date.
     */
    public List<Transaction> fetchTransactionsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return transactionRepository.findByTransactionDateBetween(startOfDay, endOfDay);
    }


    /**
     * Fetches transactions for a specific date and groups them by date for daily reports.
     *
     * @param date The date for which transactions are fetched and grouped.
     * @return List of daily transaction reports with grouped transactions for the provided date.
     */
    public List<DailyTransactionReport> fetchAndGroupTransactionsByDate(LocalDate date) {
        List<Transaction> transactions = fetchTransactionsByDate(date);

        Map<LocalDate, List<Transaction>> groupedTransactions = transactions.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getTransactionDate().toLocalDate()));

        return groupedTransactions.entrySet().stream()
                .map(entry -> new DailyTransactionReport(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches transactions within a date range.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return List of transactions within the specified date range.
     */
    public List<Transaction> fetchTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);
        return transactionRepository.findByTransactionDateBetween(startOfDay, endOfDay);
    }

    /**
     * Fetches transactions within a date range and groups them by date for daily reports.
     *
     * @param startDate The start date of the range for fetching and grouping transactions.
     * @param endDate   The end date of the range for fetching and grouping transactions.
     * @return List of daily transaction reports with grouped transactions within the specified date range.
     */
    public List<DailyTransactionReport> fetchAndGroupTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = fetchTransactionsByDateRange(startDate, endDate);

        Map<LocalDate, List<Transaction>> groupedTransactions = transactions.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getTransactionDate().toLocalDate()));

        return groupedTransactions.entrySet().stream()
                .map(entry -> new DailyTransactionReport(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    // Implement other service methods for fetching, grouping, etc.
}
