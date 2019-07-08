package smartgov.core.simulation.time;

/**
 * An incrementable instantiation of Time.
 * 
 * @author pbreugnot
 *
 */
public class Clock extends Time {
	
	/**
	 * Time origin (day = 0, hour = 0, minutes = 0, seconds = 0, week day = MONDAY)
	 * 
	 */
	public static final Date origin = new Date(0, WeekDay.MONDAY, 0, 0);
	


	/**
	 * Clock constructor, initialized at the origin.
	 */
	public Clock() {
		super(origin.getDay(), origin.getWeekDay(), origin.getHour(), origin.getMinutes());
	}
	

	/**
	 * Public alias of the {@link Time#increment} function.
	 * 
	 * @param seconds seconds to add to the clock
	 */
	public void increment(double seconds) {
		super.increment(seconds);
	}
}
