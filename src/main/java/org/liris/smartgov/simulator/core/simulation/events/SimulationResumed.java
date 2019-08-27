package org.liris.smartgov.simulator.core.simulation.events;

import org.liris.smartgov.simulator.core.simulation.time.Date;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Event triggered when the simulation is resumed.
 */
@JsonRootName(value = "resume")
public class SimulationResumed extends TimedEvent {

	/**
	 * SimulationResumed constructor.
	 *
	 * @param tick tick count at which the event occurred
	 * @param date date at which the event occurred
	 */
	public SimulationResumed(int tick, Date date) {
		super(tick, date);
	}

}
