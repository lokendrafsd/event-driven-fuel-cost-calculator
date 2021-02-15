package com.daimler.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventProducerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventProducerApiApplication.class, args);
	}

}
