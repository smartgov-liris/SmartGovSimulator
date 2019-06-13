package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Triggered when the simulation stops.
 */
@JsonRootName(value = "stop")
public class SimulationStopped extends TimedEvent {

	/**
	 * SimulationStopped constructor.
	 *
	 * @param tick tick count at which the event occured
	 */
	public SimulationStopped(int tick) {
		super(tick);
	}

}
