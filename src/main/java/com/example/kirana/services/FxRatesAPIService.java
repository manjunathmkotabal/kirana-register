package com.example.kirana.services;

import com.example.kirana.constants.enums.Currency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class FxRatesAPIService {

    public static Map<Currency, BigDecimal> fetchExchangeRates() {
        RestTemplate restTemplate = new RestTemplate();

        String fxRatesApiUrl = "https://api.fxratesapi.com/latest";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                fxRatesApiUrl,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<Currency, BigDecimal> exchangeRates = new HashMap<>();

        if (response.getBody() != null && response.getBody().containsKey("rates")) {
            Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");

            for (Map.Entry<String, Object> entry : rates.entrySet()) {
                try {
                    Currency currency = Currency.valueOf(entry.getKey());
                    BigDecimal rate;

                    if (entry.getValue() instanceof Integer) {
                        rate = BigDecimal.valueOf((Integer) entry.getValue());
                    } else if (entry.getValue() instanceof Double) {
                        rate = BigDecimal.valueOf((Double) entry.getValue());
                    } else {
                        // Handle other types or skip this entry
                        continue;
                    }

                    exchangeRates.put(currency, rate);
                } catch (IllegalArgumentException ignored) {
                    // Currency not defined in the enum, ignore or handle as needed
                }
            }
        }

        return exchangeRates;
    }
}

