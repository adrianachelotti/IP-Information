package com.mercadolibre.mercadopuntos.config;

import com.mercadolibre.mercadopuntos.converters.ZonedDateTimeReadConverter;
import com.mercadolibre.mercadopuntos.converters.ZonedDateTimeWriteConverter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
public class MongoDbConfig extends AbstractMongoClientConfiguration {

    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

    @Override
    protected String getDatabaseName() {
        return "ip_information";
    }


    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.mercadolibre.mercadopuntos");
    }


    @Override
    public MongoCustomConversions customConversions() {
        converters.add(new ZonedDateTimeWriteConverter());
        converters.add(new ZonedDateTimeReadConverter());
        return new MongoCustomConversions(converters);
    }



    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}