package com.mercadolibre.mercadopuntos.models;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class IpCountryInformationModel {

    @EmbeddedId
    private IpInformationId id;
    private String  codeIso3;
    private String name;
    private List<ZonedDateTime> timezones;
    private List<String> languages;
    private BigDecimal distanced;
    private Map<String, BigDecimal> currencies = new HashMap<>();
}
