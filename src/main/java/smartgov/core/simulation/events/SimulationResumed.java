package smartgov.core.simulation.events;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Event triggered when the simulation is resumed.
 */
@JsonRootName(value = "resume")
public class SimulationResumed extends TimedEvent {

	/**
	 * SimulationResumed constructor.
	 *
	 * @param tick tick count at which the event occured
	 */
	public SimulationResumed(int tick) {
		super(tick);
	}

}
