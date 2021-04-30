package com.mercadolibre.mercadopuntos.dtos;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CountryInfoDto {

    private String name;
    private String nativeName;
    private String alpha2Code;
    private String alpha3Code;
    private List<BigDecimal> latlng;
    private List<String> timezones;
    private List<LanguageDto> languages;
    private List<CorruncyDto> currencies;
    private Map<String, String> translations;


}
