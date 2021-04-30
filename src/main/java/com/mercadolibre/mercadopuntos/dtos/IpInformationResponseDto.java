package com.mercadolibre.mercadopuntos.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class IpInformationResponseDto {

    private String ip;
    private String  codeIso;
    private String country;
    private String date;
    private List<String> times;
    private List<String> languages;
    private String estimatedDistance;
    private Map<String, String> currencies = new HashMap<>();

}
