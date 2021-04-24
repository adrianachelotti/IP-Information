package com.mercadolibre.mercadopuntos.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class IpInformationResponseDto {

    private String ip;
    private String  codeIso3;
    private String name;
    private List<ZonedDateTime> timezones;
    private List<String> languages;
    private BigDecimal distance;
    private Map<String, BigDecimal> currencies = new HashMap<>();

}
