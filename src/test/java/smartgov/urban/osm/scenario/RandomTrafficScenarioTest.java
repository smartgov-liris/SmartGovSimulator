package smartgov.urban.osm.scenario;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.agent.moving.events.DestinationReachedEvent;
import smartgov.core.agent.moving.events.NodeReachedEvent;
import smartgov.core.environment.graph.Node;
import smartgov.core.events.EventHandler;
import smartgov.core.simulation.events.SimulationStep;
import smartgov.urban.osm.agent.behavior.RandomTrafficBehavior;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.scenario.lowLayer.RandomTrafficScenario;

public class RandomTrafficScenarioTest {
	
	public static SmartGov loadRandomTrafficScenario() {
		return new SmartGov(
				new OsmContext(
					RandomTrafficScenarioTest.class.getResource("traffic_config.properties").getFile()
					)
				);
	}

	@Test
	public void testLoadRandomTrafficScenario() {
		SmartGov smartGov = loadRandomTrafficScenario();
		
		assertThat(
			smartGov.getContext().getScenario() instanceof RandomTrafficScenario,
			equalTo(true)
				);
		
		assertThat(
			smartGov.getContext().agents.values(),
			hasSize(2)
				);
		
		for (Agent<?> agent : smartGov.getContext().agents.values()) {
			assertThat(
				agent.getBehavior() instanceof RandomTrafficBehavior,
				equalTo(true)
				);
		}
	}
	
	@Test
	public void testRandomTrafficBehavior() throws InterruptedException {
		SmartGov smartGov = loadRandomTrafficScenario();
		
		Map<String, Node> agentDestinations = new HashMap<>();
		Map<String, List<String>> agentPlans = new HashMap<>();
		
		Map<String, Boolean> firstDestinationReached = new HashMap<>();
		Map<String, Boolean> secondDestinationReached = new HashMap<>();
		
		for (Agent<?> agent : smartGov.getContext().agents.values()) {
			Node origin = ((MovingBehavior) agent.getBehavior()).getOrigin();
			Node destination = ((MovingBehavior) agent.getBehavior()).getDestination();
			
			assertThat(
					origin.getId(),
					not(equalTo(destination.getId()))
					);
			
			agentDestinations.put(agent.getId(), destination);
			agentPlans.put(agent.getId(), new ArrayList<>());
			SmartGov.logger.info("Agent " + agent.getId() + " origin : " + origin.getId());
			SmartGov.logger.info("Agent " + agent.getId() + " destination : " + destination.getId());
			
			firstDestinationReached.put(agent.getId(), false);
			secondDestinationReached.put(agent.getId(), false);
			for (Node node : ((MovingAgentBody) agent.getBody()).getPlan().getNodes()) {
				agentPlans.get(agent.getId()).add(node.getId());
			}
			
			assertThat(
					agentPlans.get(agent.getId()).size() > 1,
					equalTo(true)
					);
			
			agentPlans.get(agent.getId()).remove(origin.getId());
			
			((MovingAgentBody) agent.getBody()).addOnNodeReachedListener(new EventHandler<NodeReachedEvent>(){

				@Override
				public void handle(NodeReachedEvent event) {
					agentPlans.get(agent.getId()).remove(event.getNode().getId());
					
				}
				
			});
			
			((MovingAgentBody) agent.getBody()).addOnDestinationReachedListener(new EventHandler<DestinationReachedEvent>(){

				@Override
				public void handle(DestinationReachedEvent event) {
					SmartGov.logger.info("Agent " + agent.getId() + " reached destination : " + event.getNode().getId());
					
					if(firstDestinationReached.get(agent.getId())) {
						secondDestinationReached.put(agent.getId(), true);
					}
					else {
						firstDestinationReached.put(agent.getId(), true);
						assertThat(
								agentPlans.get(agent.getId()),
								hasSize(0)
								);
					}
					
				}
				
			});
			
//			((MovingAgentBody) agent.getBody()).addOnOriginReachedListener(new EventHandler<OriginReachedEvent>() {
//
//				@Override
//				public void handle(OriginReachedEvent event) {
//					for (Node node : ((MovingAgentBody) agent.getBody()).getPlan().getNodes()) {
//						agentPlans.get(agent.getId()).add(node.getId());
//					}
//				}
//				
//			});

		}
		
		SmartGov.getRuntime().addSimulationStepListener(new EventHandler<SimulationStep>() {

			@Override
			public void handle(SimulationStep event) {
				
				if(allDestinationReached(firstDestinationReached, secondDestinationReached)) {
					SmartGov.getRuntime().stop();
					SmartGov.logger.info("All destination has been reached.");
				}
				
			}
			
		});
		
		SmartGov.getRuntime().start(100000);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(100);
		}
		
		assertThat(
				allDestinationReached(firstDestinationReached, secondDestinationReached),
				equalTo(true)
				);
		
		
	}
	
	private static Boolean allDestinationReached(Map<String, Boolean> firstDestinationStatus, Map<String, Boolean> secondDestinationStatus) {
		Boolean allDestinationReached = true;
		for (Boolean isFirstDestinationReached : firstDestinationStatus.values()) {
			allDestinationReached = allDestinationReached && isFirstDestinationReached;
		}
		for (Boolean isSecondDestinationReached : secondDestinationStatus.values()) {
			allDestinationReached = allDestinationReached && isSecondDestinationReached;
		}
		return allDestinationReached;
	}
}
