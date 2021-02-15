package com.daimler.services.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author lokendrav
 *
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate getRestTemplateBean() {
		return new RestTemplate();
	}
}
