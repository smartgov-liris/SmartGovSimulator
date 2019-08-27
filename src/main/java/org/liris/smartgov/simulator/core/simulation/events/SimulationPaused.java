package org.liris.smartgov.simulator.core.simulation.events;

import org.liris.smartgov.simulator.core.simulation.time.Date;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Event triggered when the simulation is paused.
 */
@JsonRootName(value = "pause")
public class SimulationPaused extends TimedEvent {

	/**
	 * SimulationPaused constructor.
	 *
	 * @param tick tick count at which the event occurred
	 * @param date date at which the event occurred
	 */
	public SimulationPaused(int tick, Date date) {
		super(tick, date);
	}

}
