package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "step")
public class SimulationStep extends TimedEvent {

	public SimulationStep(int tick) {
		super(tick);
	}

}
