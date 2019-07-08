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

}
