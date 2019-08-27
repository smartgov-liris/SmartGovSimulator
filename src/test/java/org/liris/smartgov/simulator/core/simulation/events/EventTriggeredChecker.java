package org.liris.smartgov.simulator.core.simulation.events;

import org.liris.smartgov.simulator.core.events.Event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class EventTriggeredChecker {

	public boolean eventTriggered = false;
	public String json;
	
	public void buildJson(Event event) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		try {
			json = mapper.writeValueAsString(event);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
