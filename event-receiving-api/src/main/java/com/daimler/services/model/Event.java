package com.daimler.services.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {

	private boolean eventFlag;
	private String timeStamp;
	private String cityName;
}
