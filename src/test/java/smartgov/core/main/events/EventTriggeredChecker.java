package smartgov.core.main.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import smartgov.core.events.Event;

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
