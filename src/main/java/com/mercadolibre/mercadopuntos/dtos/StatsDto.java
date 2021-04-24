package com.mercadolibre.mercadopuntos.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatsDto {

    private BigDecimal distance;
    private Long invocations;
    private String isoCode3;
}
