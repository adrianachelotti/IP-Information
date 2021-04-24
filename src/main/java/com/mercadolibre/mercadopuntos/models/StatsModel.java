package com.mercadolibre.mercadopuntos.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Document
@Data
@Entity
public class  StatsModel {
    @Id
    private String codeIso3;
    private BigDecimal distance;
    private Long invocations;
}
