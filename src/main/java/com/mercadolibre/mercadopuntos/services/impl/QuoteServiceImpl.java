package com.mercadolibre.mercadopuntos.services.impl;

import com.mercadolibre.mercadopuntos.dtos.QuoteDto;
import com.mercadolibre.mercadopuntos.services.QuoteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuoteServiceImpl implements QuoteService {

    @Value("${currency.quote.url}")
    private String currencyQuoteUrl;

    @Value("${currency.quote.token}")
    private String token;

    private RestTemplate restTemplate;

    public QuoteServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public QuoteDto getQuote() {
        return restTemplate.getForObject(currencyQuoteUrl, QuoteDto.class,token);
    }

}
