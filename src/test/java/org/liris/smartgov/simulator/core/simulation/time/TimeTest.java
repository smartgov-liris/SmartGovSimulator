package org.liris.smartgov.simulator.core.simulation.time;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.liris.smartgov.simulator.core.simulation.time.Clock;
import org.liris.smartgov.simulator.core.simulation.time.Date;
import org.liris.smartgov.simulator.core.simulation.time.Time;
import org.liris.smartgov.simulator.core.simulation.time.WeekDay;


public class TimeTest {

	@Test
	public void testCompareTo() {
		Time t1 = new Date(10, WeekDay.MONDAY, 10, 10);
		Time t2 = new Date(10, WeekDay.MONDAY, 10, 10);
		
		assertThat(
				t1.compareTo(t2),
				equalTo(0)
				);
		
		t2 = new Date(10, WeekDay.MONDAY, 8, 10);
		assertThat(
				t1.compareTo(t2),
				greaterThan(0)
				);
		assertThat(
				t2.compareTo(t1),
				lessThan(0)
				);
		
		t2 = new Date(10, WeekDay.MONDAY, 10, 8);
		assertThat(
				t1.compareTo(t2),
				greaterThan(0)
				);
		assertThat(
				t2.compareTo(t1),
				lessThan(0)
				);
	}
	
	@Test
	public void timeIncrementMinutes() {
		Time time = new Date(0, WeekDay.MONDAY, 0, 0);
		
		for (int i = 0; i < 2 * 60 + 10; i++) {
			time._increment(1);
		}
		
		// Incremented time
		assertThat(
				time.getMinutes(),
				equalTo(2)
				);
		assertThat(
				time.getSeconds(),
				equalTo(10.)
				);
		
	}
	
	@Test
	public void timeIncrementHours() {
		Time time = new Date(0, WeekDay.MONDAY, 0, 0);
		
		time._increment(3 * 3600 + 3 * 60 + 10);
		
		// Incremented time
		assertThat(
				time.getHour(),
				equalTo(3)
				);

		assertThat(
				time.getMinutes(),
				equalTo(3)
				);
		
		assertThat(
				time.getSeconds(),
				equalTo(10.)
				);		
	}
	
	@Test
	public void timeIncrementDays() {
		Time time = new Date(0, WeekDay.MONDAY, 0, 0);
		
		time._increment(3 * 24 * 3600 + 3 * 3600 + 3 * 60 + 10);
		
		// Incremented time
		assertThat(
				time.getDay(),
				equalTo(3)
				);
		
		assertThat(
				time.getWeekDay(),
				equalTo(WeekDay.THURSDAY)
				);

		assertThat(
				time.getHour(),
				equalTo(3)
				);

		assertThat(
				time.getMinutes(),
				equalTo(3)
				);
		
		assertThat(
				time.getSeconds(),
				equalTo(10.)
				);		
	}
	
	@Test
	public void compareClockToDate() {
		Clock clock = new Clock();
		clock.increment(2 * 24 * 3600 + 5 * 3600 + 10 * 60 + 5);
		
		Date date = new Date(clock.getOrigin(), 2, 5, 10, 5);
		
		assertThat(
			clock,
			equalTo(date)
			);
		
		Date otherDate = new Date(clock.getOrigin(), 1, 1, 1);
		assertThat(
			clock,
			not(equalTo(otherDate))
			);
	}
}
