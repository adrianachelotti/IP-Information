package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.IpCountryDetailDto;

public interface IpCountryService {

    IpCountryDetailDto getCountryDetailForIp(String ip);

}
