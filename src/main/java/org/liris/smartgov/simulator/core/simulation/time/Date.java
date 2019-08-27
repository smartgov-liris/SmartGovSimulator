package org.liris.smartgov.simulator.core.simulation.time;

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
	
	/**
	 * Build a new date from the specified origin. The origin is just used
	 * to compute the good week day.
	 * 
	 * @param origin used to compute the corresponding week day
	 * @param day days counts
	 * @param hour hours count
	 * @param minutes minutes count
	 * @param seconds seconds
	 */
	public Date(Date origin, int day, int hour, int minutes, double seconds) {
		this(day, origin.getWeekDay().after(day), hour, minutes, seconds);
	}
	
	/**
	 * Returns a new date incremented from this date by the specified 
	 * amount of time.
	 * 
	 * <p>
	 * Each value can be in any range.
	 * </p>
	 * 
	 * @param days days to add
	 * @param hours hours to add
	 * @param minutes minutes to add
	 * @param seconds seconds to add
	 * @return new incremented date
	 */
	public Date after(int days, int hours, int minutes, int seconds) {
		Date afterDate = this.copy();
		afterDate._increment(24 * 3600 * days + 3600 * hours + 60 * minutes + seconds);
		return afterDate;
	}
	
	/**
	 * Returns a new date incremented from this date by the specified
	 * days count.
	 * 
	 * @param days days to add
	 * @return new incremented date
	 */
	public Date afterDay(int days) {
		return this.after(days, 0, 0, 0);
	}
	
	/**
	 * Returns a new date incremented from this date by the specified
	 * amounts of hours and minutes.
	 * 
	 * <p>
	 * Each value can be in any range.
	 * </p>
	 * 
	 * @param hours hours to add
	 * @param minutes minutes to add
	 * @return new incremented hour
	 */
	public Date afterHour(int hours, int minutes) {
		return this.after(0, hours, minutes, 0);
	}
	
	/**
	 * Returns the date of the same day at the specified time.
	 * The specified time must be <b>after</b> this date hour.
	 * 
	 * @param hour hour in [0, 24[
	 * @param minutes minutes in [0, 60[
	 * @return corresponding date of the same day
	 * @throws IllegalArgumentException if hours or minutes are not in a
	 * valid range, or if the specified date is anterior to this date.
	 */
	public Date at(int hour, int minutes) {
		if (hour < 0 || hour >= 24) {
			throw new IllegalArgumentException("Hours must be specified in [0, 24[");
		}
		if (minutes < 0 || minutes >= 60) {
			throw new IllegalArgumentException("Minutes must be specified in [0, 60[");
		}
		int hourToIncrement = hour - this.hour;
		int minutesToIncrement = minutes - this.minutes;
		if (hourToIncrement < 0 || minutesToIncrement < 0) {
			throw new IllegalArgumentException("The specified hour can't be anterior to this date hour.");
		}
		return this.afterHour(hourToIncrement, minutesToIncrement);
	}
	
	/**
	 * Returns a new date that correspond to the next specified week day.
	 * If the weekDay is the same as the current week day, returns the 
	 * same day of the next week.
	 * 
	 * @param weekDay next week day
	 * @return new date corresponding to the specified week day
	 */
	public Date next(WeekDay weekDay) {
		Date nextWeekDay = this.copy();
		nextWeekDay._increment(24 * 3600);
		while(nextWeekDay.getWeekDay() != weekDay) {
			nextWeekDay._increment(24 * 3600);
		}
		return nextWeekDay;
	}
	
	/**
	 * Returns a new Date instance with the same values as this date
	 * 
	 * @return this date copy
	 */
	public Date copy() {
		return new Date(this.day, this.weekDay, this.hour, this.minutes, this.seconds);
	}

}
