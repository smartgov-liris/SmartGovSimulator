package smartgov.core.simulation.events;

import com.fasterxml.jackson.annotation.JsonRootName;

import smartgov.core.simulation.time.Date;

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
