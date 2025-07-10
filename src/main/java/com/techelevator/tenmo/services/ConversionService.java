package com.techelevator.tenmo.services;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class ConversionService {

    // This is needed to allow us to reach the url that we are trying to call using:
    // String Rest api
    private RestTemplate restTemplate = new RestTemplate();

    //This allows the user to choose if they want to transfer usd to te or
    // vise versa and put in the amount they are wanting to transfer to the other
    // form of payment
    public BigDecimal conversion (String from, String to, BigDecimal amount) {
        try {
            String urlString = UriComponentsBuilder
                    .fromHttpUrl("https://api.frankfurter.dev/v1/latest")
                    .queryParam("base", from) //from needs to be usd or eur
                    .queryParam("symbols", to) //to needs to be usd or eur
                    .toUriString();
            RestClient restClient = RestClient.create();
             Map<String, Object> exchangeRates = restClient.get()
                    .uri(urlString)
                    .retrieve()
                    .body(Map.class);
             Map<String,Object> rates = (Map<String,Object>) exchangeRates.get("rates");
             BigDecimal rate = new BigDecimal(rates.get(to).toString());
             BigDecimal multipleAmount = amount.multiply(rate);
             BigDecimal finalAmount = multipleAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
             return finalAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
