package com.daimler.services.model;

import lombok.Data;

@Data
public class Event {
	private boolean eventFlag;
	private String timeStamp;
	private String cityName;
}
