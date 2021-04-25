package com.mercadolibre.mercadopuntos;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {IpInformationApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration_test")
public abstract class IntegrationTest {

	@Autowired
	protected TestRestTemplate testRestTemplate;

	@Primary
	@Bean
	public MongoTemplate mongoTemplate() {
		EmbeddedMongoFactoryBean mongoFactoryBean = new EmbeddedMongoFactoryBean();
		return new MongoTemplate((MongoDatabaseFactory) mongoFactoryBean);
	}

	protected <T> RequestEntity<T> getDefaultRequestEntity() {
		HttpHeaders headers = new HttpHeaders();
		return new RequestEntity<>(headers, HttpMethod.GET, null);
	}
}
