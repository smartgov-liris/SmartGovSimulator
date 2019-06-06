package smartgov.core.main;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import smartgov.SmartGov;
import smartgov.SmartGovTest;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.Plan;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.events.EventHandler;
import smartgov.core.main.events.SimulationStopped;

public class TestSmartGovRuntime {
	
	private SmartGovRuntime loadRuntime() {
		SmartGovTest.loadSmartGov();
		return SmartGov.getRuntime();
	}

	@Test
	public void testStartSimulation() throws InterruptedException {
		SmartGovRuntime runtime = loadRuntime();
		
		assertThat(
				runtime,
				notNullValue()
				);
		
		runtime.start(10);
		
		while(runtime.isRunning()) {
			// Wait
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		assertThat(
				runtime.getTickCount(),
				equalTo(10)
				);
	}
	
	@Test
	public void testDynamicBehavior() throws InterruptedException {
		SmartGov smartGov = SmartGovTest.loadSmartGov();

		SmartGov.getRuntime().start(7);
		
		while(SmartGov.getRuntime().isRunning()) {
			// Wait
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		Plan newPlan1 = ((MovingAgentBody) smartGov.getContext().agents.get("1").getBody()).getPlan();
		ArrayList<String> expectedIds = new ArrayList<>();
		for(Node node : newPlan1.getNodes()) {
			expectedIds.add(node.getId());
		}
		assertThat(
				expectedIds,
				equalTo(Arrays.asList("5", "3", "1"))
				);
		
		Plan newPlan2 = ((MovingAgentBody) smartGov.getContext().agents.get("2").getBody()).getPlan();
		expectedIds = new ArrayList<>();
		for(Node node : newPlan2.getNodes()) {
			expectedIds.add(node.getId());
		}
		assertThat(
				expectedIds,
				equalTo(Arrays.asList("4", "1", "2"))
				);
		
	}
}
