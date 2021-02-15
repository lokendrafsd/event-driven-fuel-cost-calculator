package com.daimler.services.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.daimler.services.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProducerService {

	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;

	@Value("${spring.kafka.template.default-topic}")
	public String topic;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * @Def: CRON job to push events at every 2 minutes to kafka broker
	 */
	@Scheduled(cron = "0 0/2 * * * *")
	public void triggerFuelEvent() {
		Event event = new Event();
		event.setEventFlag(new Random().nextBoolean());
		event.setTimeStamp(LocalDateTime.now().toString());
		event.setCityName("Mumbai");
		try {
			sendLibraryEvent(event);
		} catch (JsonProcessingException e) {
			log.error("DAIMLER: Event Producer API: Exception While triggering Fuel Event and the exception is {}", e.getMessage());
		}
	}

	/**
	 * @Def: Method to handle the advance handling of errors and on failure mechanism
	 */
	public void sendLibraryEvent(Event fuelEvent) throws JsonProcessingException {

		Integer key = 1;
		String value = objectMapper.writeValueAsString(fuelEvent);

		ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key, value, result);
			}
		});
	}

	public ListenableFuture<SendResult<Integer, String>> sendLibraryEvent_Approach2(Event fuelEvent)
			throws JsonProcessingException {

		String value = objectMapper.writeValueAsString(fuelEvent);
		int key = 1;
		ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);

		ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);

		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key, value, result);
			}
		});

		return listenableFuture;
	}

	private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {

		List<Header> recordHeaders = Arrays.asList(new RecordHeader("event-source", "scanner".getBytes()));

		return new ProducerRecord<>(topic, null, key, value, recordHeaders);
	}

	public SendResult<Integer, String> sendLibraryEventSynchronous(Event fuelEvent) throws Exception {

		Integer key = 1;
		String value = objectMapper.writeValueAsString(fuelEvent);
		SendResult<Integer, String> sendResult = null;
		try {
			sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
		} catch (ExecutionException | InterruptedException e) {
			log.error("DAIMLER: Event Producer API: ExecutionException/InterruptedException Sending the Message and the exception is {}",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("DAIMLER: Event Producer API: Exception Sending the Message and the exception is {}", e.getMessage());
			throw e;
		}

		return sendResult;

	}

	private void handleFailure(Integer key, String value, Throwable ex) {
		log.error("DAIMLER: Event Producer API: Error Sending the Message and the exception is {}", ex.getMessage());
		try {
			throw ex;
		} catch (Throwable throwable) {
			log.error("DAIMLER: Event Producer API: Error in OnFailure: {}", throwable.getMessage());
		}

	}

	private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
		log.info("DAIMLER: Event Producer API: Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value,
				result.getRecordMetadata().partition());
	}
}
