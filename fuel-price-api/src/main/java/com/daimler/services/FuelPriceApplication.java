package com.daimler.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication (exclude = { DataSourceAutoConfiguration.class })
public class FuelPriceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuelPriceApplication.class, args);
	}

}
