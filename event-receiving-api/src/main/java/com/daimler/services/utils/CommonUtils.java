package com.daimler.services.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonUtils {

	@Autowired
	RestTemplate restTemplate;

	public Map<String, Object> restServiceCall(String serviceCallIdentifier, String url,
			HttpMethod httpMethod, HttpEntity httpEntity) throws IOException {

		log.info("DAIMLER: Event Receiving API:{} service Call url {}", serviceCallIdentifier, url);
		log.info("DAIMLER: Event Receiving API:{} request header {} and body {}", serviceCallIdentifier, httpEntity.getHeaders(),
				new ObjectMapper().writeValueAsString(httpEntity.getBody()));
		ResponseEntity<Map<String, Object>> serviceResponse = null;
		try {
			serviceResponse = restTemplate.exchange(url, httpMethod, httpEntity,
					new ParameterizedTypeReference<Map<String, Object>>() {
					});
			log.info("DAIMLER: Event Receiving API:Response for {} is {}", serviceCallIdentifier, serviceResponse);

		} catch (HttpStatusCodeException ex) {
			log.error("DAIMLER: Event Receiving API:Error while {} Call : " + ex.getResponseBodyAsString(), serviceCallIdentifier);
			throw new IOException(ex.getResponseBodyAsString(), ex);
		}
		return serviceResponse.getBody();
	}

}
