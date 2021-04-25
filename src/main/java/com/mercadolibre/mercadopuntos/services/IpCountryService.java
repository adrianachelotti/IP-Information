package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.IpCountryDetailDto;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;

public interface IpCountryService {

    IpCountryDetailDto getCountryDetailForIp(String ip) throws ValidationException, DependencyException;

}
