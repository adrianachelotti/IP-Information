package com.mercadolibre.mercadopuntos.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
@Data
public class QuoteDto {

    private boolean success;
    private BigDecimal quote;
    private Long timestamp;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

}
