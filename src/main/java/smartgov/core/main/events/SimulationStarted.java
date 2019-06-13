package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

import smartgov.core.events.Event;

/**
 * Triggered when the simulation starts.
 */
@JsonRootName(value = "start")
public class SimulationStarted extends Event {

	private int maxTick;

	/**
	 * SimulationStarted constructor.
	 *
	 * @param maxTick number of ticks to run in this simulation	
	 */
	public SimulationStarted(int maxTick) {
		this.maxTick = maxTick;
	}
	
	/**
	 * Number of ticks to run in this simulation.
	 */
	public int getMaxTick() {
		return maxTick;
	}
}
