package com.mercadolibre.mercadopuntos.services;

import com.mercadolibre.mercadopuntos.dtos.QuoteDto;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;

public interface QuoteService {

    QuoteDto getQuote() throws DependencyException;
}
