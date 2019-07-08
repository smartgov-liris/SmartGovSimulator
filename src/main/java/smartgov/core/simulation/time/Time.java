package smartgov.core.simulation.time;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract time class.
 * 
 * @author pbreugnot
 *
 */
public abstract class Time implements Comparable<Time> {

	/**
	 * Available time units.
	 * 
	 * @author pbreugnot
	 *
	 */
	public enum TimeUnit {
		DAY,
		HOUR,
		MINUTE,
		SECONDS;
		
		/**
		 * Time units ordered by importance, from DAY to SECONDS.
		 * Used to compare time instances.
		 * 
		 * @return time units ordered from DAY to SECONDS
		 */
		public static List<TimeUnit> ordered() {
			return Arrays.asList(DAY, HOUR, MINUTE, SECONDS);
		}
	}	
	
	protected int day;
	protected WeekDay weekDay;
	protected int hour;
	protected int minutes;
	protected double seconds;
	
	protected Time() {
		
	}
	

	/**
	 * Time instance constructor.
	 * 
	 * Instantiated with the specified times, and <code>seconds = 0</code>.
	 * 
	 * Hour and minutes do not need to be in range [0, 60], the correct values
	 * will be automatically set up with the {@link #increment increment()} method.
	 * 
	 * @param day days count
	 * @param weekDay weekDay
	 * @param hour hours count
	 * @param minutes minutes count
	 */
	public Time(int day, WeekDay weekDay, int hour, int minutes) {
		super();
		this.seconds = 0;
		this.weekDay = weekDay;
		this.increment(day * 24 * 3600 + hour * 3600 + minutes * 60);
	}

	/**
	 * Day count
	 * @return day count
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Week day
	 * @return week day
	 */
	public WeekDay getWeekDay() {
		return weekDay;
	}

	/**
	 * Hour, in range [0, 24]
	 * @return hour in range [0, 24]
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * Minutes, in range [0, 60]
	 * @return minutes in range [0, 60]
	 */
	public int getMinutes() {
		return minutes;
	}
	
	/**
	 * Seconds, in range [0, 60].
	 * 
	 * Represented as a double, so that smaller time units
	 * can be represented through this field.
	 * 
	 * @return seconds in range [0, 60]
	 */
	public double getSeconds() {
		return seconds;
	}
	
	private double get(TimeUnit unit) {
		switch(unit) {
		case DAY:
			return getDay();
		case HOUR:
			return getHour();
		case MINUTE:
			return getMinutes();
		case SECONDS:
			return getSeconds();
		default:
			return 0.;
		}
	}
	
	/**
	 * Increments this time with the specified amount of seconds.
	 * 
	 * Notice that this function is protected. If you want to call
	 * it from other classes, you probably want to use the {@link Clock Clock}. 
	 * 
	 * @param seconds seconds counts
	 */
	protected void increment(double seconds) {
		this.seconds += seconds;
		while(this.seconds >= 60) {
			this.seconds -= 60;
			this.minutes++;
		}
		while(minutes >= 60) {
			minutes -= 60;
			this.hour++;
		}
		int days = 0;
		while(this.hour >= 24) {
			this.hour -= 24;
			days++;
			this.day++;
		}
		this.weekDay = this.weekDay.after(days);
	}

	@Override
	public int compareTo(Time arg0) {		
		for (TimeUnit unit : TimeUnit.ordered()) {
			if (this.get(unit) > arg0.get(unit)) {
				return 1;
			}
			if (this.get(unit) < arg0.get(unit)) {
				return -1;
			}
		}
		return 0;
	}
	
	

}
