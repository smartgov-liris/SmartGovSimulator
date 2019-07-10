package smartgov.core.simulation.events;

import com.fasterxml.jackson.annotation.JsonRootName;

import smartgov.core.simulation.time.Date;

/**
 * Triggered when the simulation stops.
 */
@JsonRootName(value = "stop")
public class SimulationStopped extends TimedEvent {

	/**
	 * SimulationStopped constructor.
	 *
	 * @param tick tick count at which the event occurred
	 * @param date date at which the event occurred
	 */
	public SimulationStopped(int tick, Date date) {
		super(tick, date);
	}

}
