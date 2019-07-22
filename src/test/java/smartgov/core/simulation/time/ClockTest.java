package smartgov.core.simulation.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class ClockTest {

	@Test
	public void testDelayedActions() {
		Clock clock = new Clock();
		
		List<Date> dates = Arrays.asList(
				new Date(clock.getOrigin(), 2, 2, 0),
				new Date(clock.getOrigin(), 0, 1, 30),
				new Date(clock.getOrigin(), 1, 1, 30)
				);

		List<Date> eventsTriggered = new ArrayList<>();
		
		for(Date date : dates) {
			clock.addDelayedAction(new DelayedActionHandler(
						date,
						() -> eventsTriggered.add(date)
					));
		}
		
		clock.increment((3 * 24 + 4) * 3600);
		
		assertThat(
				eventsTriggered,
				contains(dates.get(1), dates.get(2), dates.get(0))
				);
		
	}
	
	@Test
	public void testMultipleDelayedActionsAtSameDate() {
		Clock clock = new Clock();
		Date date = new Date(clock.getOrigin(), 1, 1, 0);
		
		List<Date> eventsTriggered = new ArrayList<>();
		
		clock.addDelayedAction(new DelayedActionHandler(date, () -> eventsTriggered.add(date)));
		clock.addDelayedAction(new DelayedActionHandler(date, () -> eventsTriggered.add(date)));
		
		clock.increment((24 + 1) * 3600 + 10);
		
		assertThat(
				eventsTriggered,
				contains(date, date)
				);
		
		
		
		
	}
}
