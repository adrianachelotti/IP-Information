package com.mercadolibre.mercadopuntos.services.impl;

import com.mercadolibre.mercadopuntos.dtos.IpCountryDetailDto;
import com.mercadolibre.mercadopuntos.services.IpCountryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IpCountryServiceImpl implements IpCountryService {

    @Value("${ip.information.country.url}")
    private String ipCountryDetailUrl;

    private RestTemplate restTemplate;

    public IpCountryServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    @Cacheable(value = "ipCountry", key = "#ip")
    @Override
    public IpCountryDetailDto getCountryDetailForIp(String ip) {
        return restTemplate.getForObject(ipCountryDetailUrl, IpCountryDetailDto.class,ip);
    }
}
