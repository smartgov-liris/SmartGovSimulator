package org.liris.smartgov.simulator.core.simulation.time;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.liris.smartgov.simulator.core.simulation.time.WeekDay;

public class WeekDayTest {

	@Test
	public void testComputeAfterOneWeek() {
		for (WeekDay weekDay : WeekDay.values()) {
			assertThat(
					weekDay.after(7),
					equalTo(weekDay)
					);
		}
	}
	
	@Test
	public void testAfterThursday() {
		WeekDay day = WeekDay.THURSDAY;
		
		assertThat(
				day.after(8),
				equalTo(WeekDay.FRIDAY)
				);
		
		assertThat(
				day.after(5),
				equalTo(WeekDay.TUESDAY)
				);
	}
}
