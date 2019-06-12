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
 * - 1 -> 2 -> 5
 * - 5 -> 3 -> 1
 * 
 */
public class TestNodeEvents {
	
	private TestEventsScenario runSimulation() throws InterruptedException {
		SmartGov smartGov = new SmartGov(new TestEventsContext());
		
		SmartGov.getRuntime().start(4);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		return (TestEventsScenario) smartGov.getContext().getScenario();
	}
	
	@Test
	public void testNodeReachedListenersAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().nodeReachedIds,
				contains("1", "2", "5", "5", "3", "1", "1")
				);
	}
	
	@Test
	public void testNodeAgentArrivalListenersAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().nodeAgentArrivalTriggeredIds,
				contains("1", "2", "5", "5", "3", "1", "1")
				);
	}
	
	@Test
	public void testOriginReachedListenersAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().originReachedIds,
				contains("1", "5", "1")
				);
	}
	
	@Test
	public void testNodeAgentOriginListenersAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().nodeAgentOriginTriggeredIds,
				contains("1", "5", "1")
				);
	}
	
	@Test
	public void testDestinationReachedListenersAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().destinationReachedIds,
				contains("5", "1")
				);
	}
	
	@Test
	public void testNodeAgentDestinationListenersAreTriggered() throws InterruptedException {
		
		assertThat(
				runSimulation().nodeAgentDestinationTriggeredIds,
				contains("5", "1")
				);
	}
}
