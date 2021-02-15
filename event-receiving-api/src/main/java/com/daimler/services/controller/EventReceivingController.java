package com.daimler.services.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.services.model.ActionRequest;
import com.daimler.services.model.Event;
import com.daimler.services.service.EventReceivingService;
import com.daimler.services.service.KafkaConsumerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "${app.context.path}/action")
public class EventReceivingController {

	@Autowired
	KafkaConsumerService kafkaConsumer;

	@PostMapping(value = "/trigger-event", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> triggerEvent(@RequestBody Event request) throws IOException {
		String jsonRequest = new ObjectMapper().writeValueAsString(request);
		log.info("DAIMLER: Event Receiving API: STARTED: Event Triggered Manually with payload: {} ", jsonRequest);
		kafkaConsumer.getTopics(jsonRequest);
		log.info("DAIMLER: Event Receiving API: COMPLETED: Event Execution Successful with response {}",
				"dummy response");
		return ResponseEntity.ok("dummy Response");
	}
}
