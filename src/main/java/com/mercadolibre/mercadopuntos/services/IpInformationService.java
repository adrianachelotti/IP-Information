package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.IpInformationResponseDto;
import com.mercadolibre.mercadopuntos.dtos.StatsResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;

public interface IpInformationService {

    IpInformationResponseDto getIpInformation(String ip) throws ValidationException, DependencyException;

}
