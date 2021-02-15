package com.daimler.services.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.daimler.services.constants.ApplicationConstants;
import com.daimler.services.model.Event;
import com.daimler.services.model.FuelPriceModel;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerService {

	@Value("${spring.kafka.template.default-topic}")
	public String topic;

	@Autowired
	private Gson gson;

	@Autowired
	EventReceivingService eventReceivingService;

	ConcurrentHashMap<String, String> dataMap = new ConcurrentHashMap<>();

	/**
	 * @Def:
	 * 
	 * @param fuelEvent
	 * @throws IOException
	 */
	@KafkaListener(topics = "${spring.kafka.template.default-topic}")
	public void getTopics(@RequestBody String fuelEvent) throws IOException {
		log.info("DAIMLER: Event Receiving API: Kafka event consumed is: " + fuelEvent);
		Event mappedEvent = gson.fromJson(fuelEvent, Event.class);
		fuelPriceCalculator(mappedEvent);
	}

	/**
	 * @Def: Method to Handle Events, Process the Fuel Charge
	 * 
	 * @param fuelEvent
	 * @throws IOException
	 */
	public void fuelPriceCalculator(Event fuelEvent) throws IOException {
		if (fuelEvent.isEventFlag() && !dataMap.containsKey(ApplicationConstants.EVENT_TRUE_TIMESTAMP)) {
			dataMap.put(ApplicationConstants.EVENT_TRUE_TIMESTAMP, fuelEvent.getTimeStamp());
		} else if (!fuelEvent.isEventFlag() && dataMap.containsKey(ApplicationConstants.EVENT_TRUE_TIMESTAMP)
				&& !dataMap.containsKey(ApplicationConstants.EVENT_FALSE_TIMESTAMP)) {
			dataMap.put(ApplicationConstants.EVENT_FALSE_TIMESTAMP, fuelEvent.getTimeStamp());
		}
		if (dataMap.containsKey(ApplicationConstants.EVENT_TRUE_TIMESTAMP)
				&& dataMap.containsKey(ApplicationConstants.EVENT_FALSE_TIMESTAMP)) {
			long dur = Duration.between(LocalDateTime.parse(dataMap.get(ApplicationConstants.EVENT_TRUE_TIMESTAMP)),
					LocalDateTime.parse(dataMap.get(ApplicationConstants.EVENT_FALSE_TIMESTAMP))).getSeconds();
			log.info("Duration: " + dur);
			dataMap.clear();
			log.info("fuel-event: " + fuelEvent);
			try {
				FuelPriceModel fuelPrice = eventReceivingService.triggerEvent("Mumbai");
				getFinalPrice(fuelPrice.getPrice(), dur, fuelPrice.getCurrency());
			} catch (Exception e) {
				log.error(
						"DAIMLER: Event Receiving API:Exception while getting the fuel price information and the exception is {}",
						e.getMessage());
			}

		}

	}

	/**
	 * @Def: Method to calculate and display the final fuel price
	 * 
	 * @param fuelPrice
	 * @param duration
	 */
	private void getFinalPrice(double fuelPrice, long duration, String currency) {
		log.info("DAIMLER: Event Receiving API: Final Fuel Cost is --> {} {}",
				String.format("%.2f",(ApplicationConstants.BASE_PRICE * duration) * fuelPrice), currency);
	}
}
