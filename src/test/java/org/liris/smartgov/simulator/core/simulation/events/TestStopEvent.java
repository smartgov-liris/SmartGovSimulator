package org.liris.smartgov.simulator.core.simulation.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.SmartGovTest;

public class TestStopEvent {
	
	@Test
	public void testStopTriggered() throws InterruptedException {
		SmartGovTest.loadSmartGov();
		
		EventTriggeredChecker checker = new EventTriggeredChecker();
		
		SmartGov.getRuntime().addSimulationStoppedListener((event) -> {
				checker.eventTriggered = true;
				checker.buildJson(event);
			});
		
		SmartGov.getRuntime().start(100);
		
		SmartGov.getRuntime().waitUntilSimulatioEnd();
		
		TimeUnit.SECONDS.sleep(1);
		
		assertThat(
				checker.eventTriggered,
				equalTo(true)
				);
		
		assertThat(
				checker.json,
				equalTo("{\"stop\":{\"tick\":100,\"date\":{\"day\":0,\"weekDay\":\"MONDAY\",\"hour\":\"0:1:40.0\"}}}")
				);
	}
	
}
