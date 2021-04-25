package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.StatsResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;

public interface StatsService {

    StatsResponseDto getStats() throws ValidationException;

}
