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
            Map<Currency, BigDecimal> rates = (Map<Currency, BigDecimal>) response.getBody().get("rates");
            exchangeRates.putAll(rates);
        }

        return exchangeRates;
    }
}

