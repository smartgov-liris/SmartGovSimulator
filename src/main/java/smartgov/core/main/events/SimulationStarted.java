package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

import smartgov.core.events.Event;

@JsonRootName(value = "start")
public class SimulationStarted extends Event {

	private int maxTick;
	
	public SimulationStarted(int maxTick) {
		this.maxTick = maxTick;
	}
	
	public int getMaxTick() {
		return maxTick;
	}
}
