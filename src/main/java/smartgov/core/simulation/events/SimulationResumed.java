package smartgov.core.simulation.events;

import com.fasterxml.jackson.annotation.JsonRootName;

import smartgov.core.simulation.time.Date;

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
