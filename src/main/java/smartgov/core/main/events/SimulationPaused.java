package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Event triggered when the simulation is paused.
 */
@JsonRootName(value = "pause")
public class SimulationPaused extends TimedEvent {

	/**
	 * SimulationPaused constructor.
	 *
	 * @param tick tick count at which the event occured
	 */
	public SimulationPaused(int tick) {
		super(tick);
	}

}
