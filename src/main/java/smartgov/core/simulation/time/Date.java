package smartgov.core.simulation.time;

/**
 * A fixed time class, that cannot be incremented.
 * 
 * @author pbreugnot
 *
 */
public class Date extends Time {

	/**
	 * Date constructor.
	 * 
	 * @param day days count
	 * @param weekDay week day
	 * @param hour hours count
	 * @param minutes minutes count
	 */
	public Date(int day, WeekDay weekDay, int hour, int minutes) {
		super(day, weekDay, hour, minutes);
		this.seconds = 0;
	}
	
	/**
	 * Date constructor.
	 * 
	 * @param day days count
	 * @param weekDay week day
	 * @param hour hours count
	 * @param minutes minutes count
	 * @param seconds seconds
	 */
	public Date(int day, WeekDay weekDay, int hour, int minutes, double seconds) {
		super(day, weekDay, hour, minutes);
		this.seconds = seconds;
	}
	
	/**
	 * Build a new date from the specified origin. The origin is just used
	 * to compute the good week day.
	 * 
	 * @param origin used to compute the corresponding week day
	 * @param day days counts
	 * @param hour hours count
	 * @param minutes minutes count
	 */
	public Date(Date origin, int day, int hour, int minutes) {
		this(day, origin.getWeekDay().after(day), hour, minutes);
	}

}
