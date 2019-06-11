package smartgov.core.main.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.SmartGovTest;
import smartgov.core.events.EventHandler;

public class TestStartEvent {

	@Test
	public void testStartTriggered() throws InterruptedException {
		SmartGovTest.loadSmartGov();
		
		EventTriggeredChecker checker = new EventTriggeredChecker();
		
		SmartGov.getRuntime().addSimulationStartedListener(new EventHandler<SimulationStarted>() {

			@Override
			public void handle(SimulationStarted event) {
				checker.eventTriggered = true;
				checker.buildJson(event);
			}
			
		});
		
		SmartGov.getRuntime().start(1000);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		assertThat(
				checker.eventTriggered,
				equalTo(true)
				);
		
		assertThat(
				checker.json,
				equalTo("{\"start\":{\"maxTick\":1000}}")
				);
	}
}
