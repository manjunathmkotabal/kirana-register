package com.example.kirana.schemas;

        import com.example.kirana.constants.enums.Currency;
        import lombok.NonNull;
        import org.jetbrains.annotations.NotNull;

        import java.math.BigDecimal;

public class RecordTransactionRequest {
    @NonNull
    private BigDecimal amount;

    @NonNull
    private Currency currency;

    public RecordTransactionRequest() {
        this.amount = BigDecimal.ZERO;
        this.currency = Currency.INR;
    }

    public @NotNull BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NotNull BigDecimal amount) {
        this.amount = amount;
    }

    public @NotNull Currency getCurrency() {
        return currency;
    }

    public void setCurrency(@NotNull Currency currency) {
        this.currency = currency;
    }
    // Getters and setters...
}
