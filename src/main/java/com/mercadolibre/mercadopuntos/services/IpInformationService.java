package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.IpInformationResponseDto;
import com.mercadolibre.mercadopuntos.dtos.StatsResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;

public interface IpInformationService {

    IpInformationResponseDto getIpInformationResponse(String ip) throws ValidationException;

    StatsResponseDto getStats() throws ValidationException;
}
