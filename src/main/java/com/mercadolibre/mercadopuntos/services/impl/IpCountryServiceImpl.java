package com.mercadolibre.mercadopuntos.services.impl;

import com.mercadolibre.mercadopuntos.dtos.IpCountryDetailDto;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.services.IpCountryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class IpCountryServiceImpl implements IpCountryService {

    public static final String COUNTRY_NOT_FOUND = "Country not found";

    @Value("${ip.information.country.url}")
    private String ipCountryDetailUrl;

    private RestTemplate restTemplate;

    public IpCountryServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    @Cacheable(value = "ipCountry", key = "#ip")
    @Override
    public IpCountryDetailDto getCountryDetailForIp(String ip) throws ValidationException, DependencyException {
        try {
            IpCountryDetailDto ipCountryDetailDto = restTemplate.getForObject(ipCountryDetailUrl, IpCountryDetailDto.class, ip);
            validateCodeISO3(ipCountryDetailDto);
            return ipCountryDetailDto;
        }catch (ValidationException e){
            throw e;
        }catch (Exception exception){
            throw new DependencyException();
        }

    }
    private void validateCodeISO3(IpCountryDetailDto ipCountryDetailDto) throws ValidationException {
        if(Objects.isNull(ipCountryDetailDto) || Objects.isNull(ipCountryDetailDto.getCountryCode3())){
            throw new ValidationException(COUNTRY_NOT_FOUND);
        }
    }
}
