package com.mercadolibre.mercadopuntos.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.mercadopuntos.IntegrationTest;
import com.mercadolibre.mercadopuntos.dtos.IpInformationRequestDto;
import com.mercadolibre.mercadopuntos.dtos.IpInformationResponseDto;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

public class IpInformationControllerTest extends IntegrationTest {
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void retrieveIpInformation_withValidIp_return200() throws JsonProcessingException {
		RequestEntity<IpInformationRequestDto> requestEntity = new RequestEntity<>(objectMapper.readValue(INPUT_DATA_SAMPLE, IpInformationRequestDto.class), HttpMethod.POST, URI.create("/trace"));
		ResponseEntity<IpInformationResponseDto> responseEntity = this.testRestTemplate.exchange(requestEntity, IpInformationResponseDto.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().getIp(), "5.6.7.8");
		assertEquals(responseEntity.getBody().getCodeIso3(), "DEU");
		assertEquals(responseEntity.getBody().getCountry(), "Germany");
	}

	private static final String INPUT_DATA_SAMPLE = "{\"ip\": \"5.6.7.8\"}";

}
