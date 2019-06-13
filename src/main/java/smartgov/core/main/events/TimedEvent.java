package smartgov.core.main.events;

import smartgov.core.events.Event;
/**
 * Abstract Event to represent events that occured
 * at a defined number of ticks in the simulation.
 */
public abstract class TimedEvent extends Event {
	
	private int tick;

	/**
	 * TimedEvent constructor.
	 *
	 * @param tick tick count at which the event occured
	 */
	public TimedEvent(int tick) {
		super();
		this.tick = tick;
	}

	/**
	 * Tick count at which the event occured.
	 */
	public int getTick() {
		return tick;
	}

}
