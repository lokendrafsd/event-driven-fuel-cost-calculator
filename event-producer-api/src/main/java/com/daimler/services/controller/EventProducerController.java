package com.daimler.services.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.services.model.Event;
import com.daimler.services.service.ProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "${app.context.path}/action")
public class EventProducerController {

	@Autowired
	ProducerService producerService;

	@PostMapping(value = "/trigger-event", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> triggerEvent(@RequestBody Event request) throws IOException {
		log.info("DAIMLER: Event Producer API: STARTED: Fuel Event Triggered Manually with payload: {} ",
				new ObjectMapper().writeValueAsString(request));
		producerService.sendLibraryEvent(request);
		log.info("DAIMLER: Event Producer API: COMPLETED: Event Execution Successful with response {}",
				"dummy response");
		return ResponseEntity.status(HttpStatus.CREATED).body(request);
	}
	
}
