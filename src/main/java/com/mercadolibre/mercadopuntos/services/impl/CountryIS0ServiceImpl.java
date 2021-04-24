package com.mercadolibre.mercadopuntos.services.impl;

import com.mercadolibre.mercadopuntos.dtos.CountryInfoDto;
import com.mercadolibre.mercadopuntos.services.CountryIsoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CountryIS0ServiceImpl implements CountryIsoService {


    @Value("${country.detail.url}")
    private String countryDetailUrl;

    private RestTemplate restTemplate;

    public CountryIS0ServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "countries", key = "#codeIso3")
    @Override
    public CountryInfoDto getCountryIsoInfoDetail(String codeIso3) {
        return restTemplate.getForObject(countryDetailUrl, CountryInfoDto.class,codeIso3);
    }
}
