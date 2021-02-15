package com.daimler.services.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FuelPriceModel {

	private String cityName;
	private double price;
	private String fuelType;
	private String currency;

}
