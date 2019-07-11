package smartgov.core.simulation.time;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DateTest {
	
	public static final Date testDate = new Date(1, WeekDay.TUESDAY, 13, 10, 30);


	@Test
	public void testCopy() {
		Date copy = testDate.copy();
		
		assertThat(
				copy,
				equalTo(testDate)
				);
	}
	
	@Test
	public void testAfter() {
		Date after = testDate.after(1, 12, 2, 10);
		
		assertThat(
				after,
				equalTo(new Date(3, WeekDay.THURSDAY, 1, 12, 40.))
				);
	}
	
	@Test
	public void testAfterDay() {
		Date afterThreeDays = testDate.afterDay(3);
		
		assertThat(
				afterThreeDays,
				equalTo(new Date(4, WeekDay.FRIDAY, testDate.getHour(), testDate.getMinutes(), testDate.getSeconds()))
				);
		
	}
	
	@Test
	public void testNextWeekDay() {
		Date nextMonday = testDate.next(WeekDay.MONDAY);
		
		assertThat(
				nextMonday,
				equalTo(new Date(7, WeekDay.MONDAY, testDate.getHour(), testDate.getMinutes(), testDate.getSeconds()))
				);
		
		Date nextTuesday = testDate.next(WeekDay.TUESDAY);
		
		assertThat(
				nextTuesday,
				equalTo(new Date(8, WeekDay.TUESDAY, testDate.getHour(), testDate.getMinutes(), testDate.getSeconds()))
				);
	}
	
	@Test
	public void testAt() {
		Date at = testDate.at(15, 25);
		
		assertThat(
				at,
				equalTo(new Date(testDate.getDay(), testDate.getWeekDay(), 15, 25, testDate.getSeconds()))
				);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAnteriorHourThrowsException() {
		testDate.at(10, 0);
	}
}
