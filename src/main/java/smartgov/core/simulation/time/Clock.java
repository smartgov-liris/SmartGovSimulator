package smartgov.core.simulation.time;

import java.util.TreeSet;

/**
 * An incrementable instantiation of Time.
 * 
 * @author pbreugnot
 *
 */
public class Clock extends Time {
	
	private TreeSet<DelayedActionHandler> actions;
	
	/**
	 * Time origin (day = 0, hour = 0, minutes = 0, seconds = 0, week day = MONDAY)
	 * 
	 */
	public static final Date origin = new Date(0, WeekDay.MONDAY, 0, 0);
	
	private Date clockOrigin;


	/**
	 * Clock constructor, initialized at the default origin.
	 */
	public Clock() {
		this(origin);
	}
	
	/**
	 * Creates a clock from the specified origin.
	 * 
	 * @param origin time origin to consider
	 */
	public Clock(Date origin) {
		super(origin.getDay(), origin.getWeekDay(), origin.getHour(), origin.getMinutes());
		this.clockOrigin = origin;
		this.actions = new TreeSet<>();
	}
	
	/**
	 * Clock origin.
	 * @return clock origin
	 */
	public Date getOrigin() {
		return clockOrigin;
	}
	

	/**
	 * Increments this clock with the given amount of seconds,
	 * and triggers delayed actions that should occur during
	 * this time.
	 * 
	 * @param seconds seconds to add to the clock
	 */
	public void increment(double seconds) {
		super._increment(seconds);
		while(actions.size() > 0 && this.compareTo(actions.first().getDate()) >= 0) {
			actions.pollFirst().getAction().trigger();
		}
	}
	
	/**
	 * Reset the clock to its origin value and clears
	 * registered delayed actions.
	 */
	public void reset() {
		this.day = clockOrigin.getDay();
		this.weekDay = clockOrigin.getWeekDay();
		this.hour = clockOrigin.getHour();
		this.minutes = clockOrigin.getMinutes();
		this.actions.clear();
	}
	
	public void addDelayedAction(DelayedActionHandler action) {
		actions.add(action);
	}
}
