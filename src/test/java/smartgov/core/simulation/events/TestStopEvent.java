package smartgov.core.simulation.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.SmartGovTest;
import smartgov.core.events.EventHandler;
import smartgov.core.simulation.events.SimulationStopped;

public class TestStopEvent {
	
	@Test
	public void testStopTriggered() throws InterruptedException {
		SmartGovTest.loadSmartGov();
		
		EventTriggeredChecker checker = new EventTriggeredChecker();
		
		SmartGov.getRuntime().addSimulationStoppedListener(new EventHandler<SimulationStopped>() {

			@Override
			public void handle(SimulationStopped event) {
				checker.eventTriggered = true;
				checker.buildJson(event);
			}
			
		});
		
		SmartGov.getRuntime().start(100);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		TimeUnit.SECONDS.sleep(1);
		
		assertThat(
				checker.eventTriggered,
				equalTo(true)
				);
		
		assertThat(
				checker.json,
				equalTo("{\"stop\":{\"tick\":100}}")
				);
	}
	
}
