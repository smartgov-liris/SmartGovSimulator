package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "stop")
public class SimulationStopped extends TimedEvent {

	public SimulationStopped(int tick) {
		super(tick);
	}

}
