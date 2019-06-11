package smartgov.core.main.events;

import smartgov.core.events.Event;

public class TimedEvent extends Event {
	
	private int tick;

	public TimedEvent(int tick) {
		super();
		this.tick = tick;
	}

	public int getTick() {
		return tick;
	}

}
