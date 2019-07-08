package smartgov.urban.geo.scenario;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.events.MoveEvent;
import smartgov.core.agent.moving.events.NodeReachedEvent;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.events.EventHandler;
import smartgov.urban.geo.SmartGovGeoTest;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.utils.GISComputation;

public class TestGeoScenario {
	
	@Test
	public void testLoadScenario() {
		SmartGovGeoTest smartGov = new SmartGovGeoTest();
		
		assertThat(
				smartGov.getContext().nodes.values(),
				hasSize(4)
				);
		
		assertThat(
				smartGov.getContext().arcs.values(),
				hasSize(4)
				);
		
		for (Node node : smartGov.getContext().nodes.values()) {
			assertThat(
					node instanceof GeoNode,
					equalTo(true)
					);
		}
		
		for (Arc arc : smartGov.getContext().arcs.values()) {
			assertThat(
					arc instanceof GeoArc,
					equalTo(true)
					);
		}
	}

	@Test
	public void testArcLength() {
		SmartGovGeoTest smartGov = new SmartGovGeoTest();
		
		for (Arc arc : smartGov.getContext().arcs.values()) {
			assertThat(
					arc.getLength(),
					equalTo(GISComputation.GPS2Meter(
							((GeoNode) arc.getStartNode()).getPosition(),
							((GeoNode) arc.getTargetNode()).getPosition()
							)
							)
					);
		}
	}
	
	@Test
	public void testAgentIsMoving() throws InterruptedException {
		SmartGovGeoTest smartGov = new SmartGovGeoTest();
		
		ArrayList<String> nodeCrossedIds = new ArrayList<>();
		
		((MovingAgentBody) smartGov.getContext().agents.get("1").getBody())
			.addOnNodeReachedListener(new EventHandler<NodeReachedEvent>() {

				@Override
				public void handle(NodeReachedEvent event) {
					nodeCrossedIds.add(event.getNode().getId());
					
				}
				
			});
		
		double roundLength = 0;
		for(Arc arc : smartGov.getContext().arcs.values()) {
			roundLength += ((GeoArc) arc).getLength();
		}
		
		double roundTimeInSeconds = roundLength / 1; // speed = 1 m/s for now
		
		int roundTimeInTicks = (int) Math.floor(roundTimeInSeconds / SmartGov.getRuntime().getTickDuration()) + 1;
		
		// Round time is realist (several ticks per arc)
		assertThat(
				roundTimeInTicks,
				greaterThan(4)
				);

		SmartGovGeoTest.getRuntime().start(2 * roundTimeInTicks);
		
		while(SmartGovGeoTest.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(100);
		}
		
		SmartGov.logger.info("Node crossed : " + nodeCrossedIds);
		
		assertThat(
				nodeCrossedIds,
				equalTo(Arrays.asList("2", "3", "3", "4", "1", "1", "2", "3", "3", "4", "1", "1"))
				);
	}
	
	@Test
	public void testMoveEventsAreTriggered() throws InterruptedException {
		SmartGovGeoTest smartGov = new SmartGovGeoTest();
		
		// To let the time to the listener to register the correct tick count
		SmartGov.getRuntime().setTickDelay(3);
		
		double roundLength = 0;
		for(Arc arc : smartGov.getContext().arcs.values()) {
			roundLength += ((GeoArc) arc).getLength();
		}
		
		double roundTimeInSeconds = roundLength / 1; // speed = 1 m/s for now
		
		int roundTimeInTicks = (int) Math.floor(roundTimeInSeconds / SmartGov.getRuntime().getTickDuration()) + 1;
		
		
		ArrayList<Integer> moveEventTicks = new ArrayList<>();
		
		((MovingAgentBody) smartGov.getContext().agents.get("1").getBody())
			.addOnMoveListener(new EventHandler<MoveEvent>() {

				@Override
				public void handle(MoveEvent event) {
					moveEventTicks.add(SmartGov.getRuntime().getTickCount());
					
				}
				
			});
		

		

		

		SmartGovGeoTest.getRuntime().start(2 * roundTimeInTicks);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		/*
		 * With our behavior, a MoveEvent should be triggered at each tick.
		 */
		ArrayList<Integer> ticksList = new ArrayList<>();
		for (int i = 0; i < 2 * roundTimeInTicks; i++) {
			ticksList.add(i);
		}
		
		assertThat(
				moveEventTicks,
				contains(ticksList.toArray())
				);
	}
	
}
