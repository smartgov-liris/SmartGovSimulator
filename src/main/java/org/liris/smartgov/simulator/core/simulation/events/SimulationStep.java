package org.liris.smartgov.simulator.core.simulation.events;

import org.liris.smartgov.simulator.core.simulation.time.Date;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Triggered at each simulation step.
 */
@JsonRootName(value = "step")
public class SimulationStep extends TimedEvent {

	/**
	 * SimulationStep constructor.
	 *
	 * @param tick tick count at which the event occurred
	 * @param date date at which the event occurred
	 */
	public SimulationStep(int tick, Date date) {
		super(tick, date);
	}

}
