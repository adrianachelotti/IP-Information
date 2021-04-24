package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.CountryInfoDto;

public interface CountryIsoService {

    CountryInfoDto getCountryIsoInfoDetail(String codeIso3);

}
