package org.liris.smartgov.simulator.core.simulation.time;

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
	 * @param day days count
	 * @param weekDay weekDay
	 * @param hour hours count
	 * @param minutes minutes count
	 * @throws IllegalArgumentException when hours or minutes are not in a valid range
	 */
	public Time(int day, WeekDay weekDay, int hour, int minutes) {
		super();
		if (hour < 0 || hour >= 24) {
			throw new IllegalArgumentException("Hours must be specified in [0, 24[");
		}
		if (minutes < 0 || minutes >= 60) {
			throw new IllegalArgumentException("Minutes must be specified in [0, 60[");
		}
		this.seconds = 0;
		this.day = day;
		this.weekDay = weekDay;
		this.hour = hour;
		this.minutes = minutes;
		// this._increment(day * 24 * 3600 + hour * 3600 + minutes * 60);
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
	protected void _increment(double seconds) {
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + hour;
		result = prime * result + minutes;
		long temp;
		temp = Double.doubleToLongBits(seconds);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((weekDay == null) ? 0 : weekDay.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Time))
			return false;
		Time other = (Time) obj;
		if (day != other.day)
			return false;
		if (hour != other.hour)
			return false;
		if (minutes != other.minutes)
			return false;
		if (Double.doubleToLongBits(seconds) != Double.doubleToLongBits(other.seconds))
			return false;
		if (weekDay != other.weekDay)
			return false;
		return true;
	}
	
	
	
	

}
