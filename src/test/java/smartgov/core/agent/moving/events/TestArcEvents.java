package smartgov.core.agent.moving.events;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.core.agent.moving.events.scenario.TestEventsContext;
import smartgov.core.agent.moving.events.scenario.TestEventsScenario;

/*
 * Test that all arc relative events are triggered, thanks to the
 * shuttle test scenario.
 * 
 * To do so, we will focus on the shuttle 1, that goes from node 1 to 5.
 * 
 * In terms of arcs, it has this trajectory :
 * - 1 -> 2
 * - 4 -> 8
 * 
 */
public class TestArcEvents {
	
	private TestEventsScenario runSimulation() throws InterruptedException {
		SmartGov smartGov = new SmartGov(new TestEventsContext());
		
		SmartGov.getRuntime().start(7);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		return (TestEventsScenario) smartGov.getContext().getScenario();
	}

	@Test
	public void testArcReachedEventsAreTriggered() throws InterruptedException {

		assertThat(
				runSimulation().arcReachedIds,
				contains("1", "2", "4", "8", "1", "2", "4", "8")
				);
	}
	
	@Test
	public void testAgentArrivalEventsAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().arcAgentArrivalTriggeredIds,
				contains("1", "2", "4", "8", "1", "2", "4", "8")
				);
	}
	
	@Test
	public void testArcLeftEventsAreTriggered() throws InterruptedException {

		assertThat(
				runSimulation().arcLeftIds,
				contains("1", "2", "4", "8", "1", "2", "4")
				);
	}
	
	@Test
	public void testAgentDepartureEventsAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().arcAgentDepartureTriggeredIds,
				contains("1", "2", "4", "8", "1", "2", "4")
				);
	}
}
