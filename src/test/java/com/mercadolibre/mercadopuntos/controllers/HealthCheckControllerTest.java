package com.mercadolibre.mercadopuntos.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mercadolibre.mercadopuntos.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HealthCheckControllerTest extends IntegrationTest {

	@Test
	void ping() {
		ResponseEntity<String> responseEntity = this.testRestTemplate.getForEntity("/healthCheck", String.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("OK", responseEntity.getBody());
	}
}
