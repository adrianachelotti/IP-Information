package com.mercadolibre.mercadopuntos.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.ZonedDateTime;
@Data
@AllArgsConstructor
@Embeddable
public class IpInformationId implements Serializable {
    private String ip;
    private ZonedDateTime insertTime;
}
