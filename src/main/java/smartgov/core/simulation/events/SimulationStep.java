package smartgov.core.simulation.events;

import com.fasterxml.jackson.annotation.JsonRootName;

import smartgov.core.simulation.time.Date;

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
