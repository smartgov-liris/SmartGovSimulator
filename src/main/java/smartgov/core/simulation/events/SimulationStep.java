package smartgov.core.simulation.events;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Triggered at each simulation step.
 */
@JsonRootName(value = "step")
public class SimulationStep extends TimedEvent {

	/**
	 * SimulationStep constructor.
	 *
	 * @param tick tick count at which the event occured
	 */
	public SimulationStep(int tick) {
		super(tick);
	}

}
