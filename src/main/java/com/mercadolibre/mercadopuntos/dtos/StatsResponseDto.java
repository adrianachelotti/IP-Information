package com.mercadolibre.mercadopuntos.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StatsResponseDto {

    private StatsDto minStatsDto;

    private StatsDto maxStatsDto;

    private String averageDistance;
}
