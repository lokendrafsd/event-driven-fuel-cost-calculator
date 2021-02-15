package com.daimler.services.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.daimler.services.constants.ApplicationConstants;
import com.daimler.services.model.FuelPriceModel;
import com.daimler.services.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableCaching
@Slf4j
public class EventReceivingService {

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	CacheManager cacheManager;

	@Value("${spring.kafka.template.default-topic}")
	public String topic;

	@Value("${fuel-api.url}")
	public String fuelApiUrl;

	/**
	 * @Def: Method to make fetch the fuel price details by calling fuel price api
	 */
	@Cacheable(value = ApplicationConstants.FUEL_CHARGES_CACHE, key = "'fuelChargeCache'+#cityName")
	public FuelPriceModel triggerEvent(String cityName) throws IOException {
		String url = fuelApiUrl.concat(cityName);
		log.info("DAIMLER: Event Receiving API: Calling Fuel Charge API with cityName: {}", cityName);
		Map<String, Object> responseMap = commonUtils.restServiceCall("get-fuel-price", url, HttpMethod.GET,
				new HttpEntity<>(cityName));
		return new ObjectMapper().convertValue(responseMap, FuelPriceModel.class);
	}

	/**
	 * @Def: Method to Clear Cached data after every 25 minutes interval
	 */
	@Scheduled(cron = "0 0/25 * * * *")
	public void clearFuelChargeData() {
		log.info("DAIMLER: Event Receiving API: Clearing Fuel Cache Data");
		cacheManager.getCache(ApplicationConstants.FUEL_CHARGES_CACHE).clear();
	}
}
