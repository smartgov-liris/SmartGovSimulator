package org.liris.smartgov.simulator.core.simulation.events;

import org.liris.smartgov.simulator.core.events.Event;

import com.fasterxml.jackson.annotation.JsonRootName;

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
	 *
	 * @return simulation max tick count
	 */
	public int getMaxTick() {
		return maxTick;
	}
}
