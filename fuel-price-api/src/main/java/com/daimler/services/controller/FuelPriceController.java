package com.daimler.services.controller;

import java.text.DecimalFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.services.model.FuelPriceModel;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/fuelPrice/v1")
@Slf4j
public class FuelPriceController {

	/**
	 * @Def: Mocked Method which will return random fuel price for any given
	 *       cityName (No Business Validations)
	 * 
	 * @param cityName
	 * @return
	 */
	@GetMapping("/{cityName}")
	public ResponseEntity<FuelPriceModel> getFuelPriceByCityName(@PathVariable(value = "cityName") String cityName) {
		log.info("STARTED: Fuel Price API: API call to getFuelPriceByCityName with cityname: {} ", cityName);
		FuelPriceModel response = new FuelPriceModel();
		DecimalFormat df = new DecimalFormat("0.00");
		response.setCityName(cityName);
		response.setCurrency("INR");
		response.setFuelType("Petrol");
		response.setPrice(Double.valueOf(df.format(70 + Math.random() * (90 - 70))));
		log.info("COMPLETED: Fuel Price API: API call to getFuelPriceByCityName with response: {} ", response);
		return ResponseEntity.ok().body(response);
	}
}
