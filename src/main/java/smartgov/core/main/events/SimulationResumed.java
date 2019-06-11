package smartgov.core.main.events;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "resume")
public class SimulationResumed extends TimedEvent {

	public SimulationResumed(int tick) {
		super(tick);
	}

}
