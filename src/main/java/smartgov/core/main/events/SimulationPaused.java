package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "pause")
public class SimulationPaused extends TimedEvent {

	public SimulationPaused(int tick) {
		super(tick);
	}

}
