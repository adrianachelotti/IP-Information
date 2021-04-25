package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.CountryInfoDto;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;

public interface CountryIsoService {

    CountryInfoDto getCountryIsoInfoDetail(String codeIso3) throws DependencyException;

}
