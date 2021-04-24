package com.mercadolibre.mercadopuntos.dtos;

import lombok.Data;

@Data
public class LanguageDto {

    private String iso639_1;
    private String iso639_2;
    private String name;
    private String nativeName;
}
