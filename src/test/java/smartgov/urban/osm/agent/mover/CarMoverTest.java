package smartgov.urban.osm.agent.mover;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.*;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.scenario.Scenario;
import smartgov.testUtils.EventChecker;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.scenario.BasicOsmScenario;

/*
 * The basic movements of the car and the steering model is tested
 * in the GippsSteeringTest.
 * 
 * Here, we will check if road changes are handled properly in specific
 * a specific situation with two roads :
 * - 1 -> 2 -> 4
 * - 3 -> 4 -> 1
 * Where nodes [1, 2, 3, 4] represent a square.
 */
public class CarMoverTest {

	private static SmartGov loadScenario() {
		return new SmartGov(new CarMoverContext());
	}
	
	@Test
	public void testLoadScenario() {
		SmartGov smartGov = loadScenario();
		
		assertThat(
				((OsmContext) smartGov.getContext()).roads.values(),
				hasSize(2)
				);
		
		Road road1 = ((OsmContext) smartGov.getContext()).roads.get("1");
		Road road2 = ((OsmContext) smartGov.getContext()).roads.get("2");
		
		assertThat(
				road1.getNodes(),
				contains("1", "2", "4")
				);
		assertThat(
				road2.getNodes(),
				contains("3", "4", "1")
				);
	}
	
	@Test
	public void testRoadChange() throws InterruptedException {
		SmartGov smartGov = loadScenario();
		
		OsmAgentBody testedAgent = (OsmAgentBody) smartGov.getContext().agents.get("1").getBody();
		
		EventChecker node4reached = new EventChecker();
		EventChecker firstMoveAfterRoadChanged = new EventChecker();
		
		Road road1 = ((OsmContext) smartGov.getContext()).roads.get("1");
		Road road2 = ((OsmContext) smartGov.getContext()).roads.get("2");
		
		testedAgent.addOnNodeReachedListener((event) -> {
			if(event.getNode().getId().equals("4")) {
				if(!node4reached.hasBeenTriggered()) {
					assertThat(
							((OsmArc) testedAgent.getPlan().getCurrentArc()).getRoad(),
							equalTo(road2)
							);
					assertThat(
							road2.getForwardAgents(),
							contains(testedAgent)
							);
					assertThat(
							road1.getForwardAgents(),
							hasSize(0)
							);

					node4reached.check();
					
					testedAgent.addOnMoveListener((moveEvent) -> {
						if(!firstMoveAfterRoadChanged.hasBeenTriggered()) {
							assertThat(
									road1.getForwardAgents(),
									hasSize(0)
									);
							assertThat(
									road2.getForwardAgents(),
									contains(testedAgent)
									);
							assertThat(
									testedAgent.getCurrentRoad(),
									equalTo(road2)
									);
							firstMoveAfterRoadChanged.check();
						}
					});
				}
			}
		});
		
		testedAgent.addOnDestinationReachedListener((event) -> {
			if(event.getNode().getId().equals("1")) {
				SmartGov.getRuntime().stop();
			}
		});
		
		SmartGov.getRuntime().start(1000);
		
		SmartGov.getRuntime().waitUntilSimulatioEnd();
		
		assertThat(
				node4reached.hasBeenTriggered(),
				equalTo(true)
				);

		assertThat(
				firstMoveAfterRoadChanged.hasBeenTriggered(),
				equalTo(true)
				);
	}
	
	private static class CarMoverContext extends OsmContext {

		public CarMoverContext() {
			super(CarMoverTest.class.getResource("car_mover_test.properties").getFile());
		}
		
		@Override
		public Scenario loadScenario(String name) {
			return new CarMoverScenario();
		}
		
	}
	
	private static class CarMoverScenario extends BasicOsmScenario {

		@Override
		public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
			OsmAgentBody body = new OsmAgentBody(
					new CarMover(
							CarMoverTestScenario.maximumAcceleration,
							CarMoverTestScenario.maximumBraking,
							CarMoverTestScenario.followerMaxSpeed,
							CarMoverTestScenario.vehicleSize
							)
					);
			OsmAgent agent = new OsmAgent(
					"1",
					body,
					new TestBehavior(body, context)
					);
			return Arrays.asList(agent);
		}
		
	}
	
	private static class TestBehavior extends MovingBehavior {
		
		private Boolean destinationReached1 = false;
		private Boolean destinationReached2 = false;

		public TestBehavior(MovingAgentBody agentBody, SmartGovContext context) {
			super(
				agentBody,
				context.nodes.get("3"),
				context.nodes.get("2"),
				context);
			agentBody.addOnDestinationReachedListener((event) -> {
				if (destinationReached1) {
					destinationReached2 = true;
				}
				else {
					destinationReached1 = true;
					refresh(context.nodes.get("2"), context.nodes.get("1"));
				}
			});
		}

		@Override
		public MoverAction provideAction() {
			if (destinationReached2)
				return MoverAction.WAIT();
			return MoverAction.MOVE();
		}
		
	}
}
