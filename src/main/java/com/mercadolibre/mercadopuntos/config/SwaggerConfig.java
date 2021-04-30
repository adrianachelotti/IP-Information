package com.mercadolibre.mercadopuntos.config;


import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(Collection.class, ZonedDateTime.class),
                        typeResolver.resolve(Collection.class, Date.class), Ordered.HIGHEST_PRECEDENCE))
                .select()
                .apis(
                        RequestHandlerSelectors
                                .basePackage("com.mercadolibre.mercadopuntos.controllers"))
                .paths(regex("/.*"))
                .build();

    }


}