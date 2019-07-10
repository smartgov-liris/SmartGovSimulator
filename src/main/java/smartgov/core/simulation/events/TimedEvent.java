package smartgov.core.simulation.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.events.Event;
import smartgov.core.simulation.time.Date;
/**
 * Abstract Event to represent events that occured
 * at a defined number of ticks in the simulation.
 */
public abstract class TimedEvent extends Event {
	
	private int tick;

	@JsonSerialize(using = CustomDateSerializer.class)
	private Date date;

	/**
	 * TimedEvent constructor.
	 *
	 * @param tick tick count at which the event occurred
	 * @param date date at which the event occurred
	 */
	public TimedEvent(int tick, Date date) {
		super();
		this.tick = tick;
		this.date = date;
	}

	/**
	 * Tick count at which the event occurred.
	 *
	 * @return event tick count
	 */
	public int getTick() {
		return tick;
	}
	
	/**
	 * Date at which the event occurred.
	 *
	 * @return event date
	 */
	public Date getDate() {
		return date;
	}

}
