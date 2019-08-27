package org.liris.smartgov.simulator.urban.osm.agent;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.agent.moving.ParkingArea;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MoverAction;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MovingBehavior;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.testUtils.EventChecker;
import org.liris.smartgov.simulator.urban.osm.agent.OsmAgent;
import org.liris.smartgov.simulator.urban.osm.agent.OsmAgentBody;
import org.liris.smartgov.simulator.urban.osm.agent.mover.CarMover;
import org.liris.smartgov.simulator.urban.osm.environment.OsmContext;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;
import org.liris.smartgov.simulator.urban.osm.scenario.BasicOsmScenario;

public class OsmAgentBodyTest {
	
	private static enum TEST_MODE {
		TEST1,
		TEST2
		};
	
	private static SmartGov loadSmartGov(TEST_MODE test) {
		switch(test) {
		case TEST1:
			return new SmartGov(new Test1Context());
		case TEST2:
			return new SmartGov(new Test2Context());
		default:
			return null;
		}
		
	}
	
	private static class TestScenario extends BasicOsmScenario {
		
		private TEST_MODE testMode;
		
		public TestScenario(TEST_MODE testMode) {
			this.testMode = testMode;
		}

		@Override
		public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
			/*
			 * Mock the ParkingArea interface : doesn't change anything because we
			 * are just interesting by events and agents behavior.
			 * We want to check if the agent correctly leaves the road and get back
			 * to it : we don't care about what it could do in a ParkingArea.
			 */
			ParkingArea fakeParking = mock(ParkingArea.class);
			
			OsmAgentBody agentBody = new OsmAgentBody(new CarMover(4, -6, 6, 8));
			
			MovingBehavior behavior = null;
			switch(testMode){
			case TEST1:
				behavior = new TestBehavior1( // Custom parking behavior
						agentBody,
						fakeParking,
						context
						);
				break;
			case TEST2:
				behavior = new TestBehavior2( // Custom parking behavior
						agentBody,
						fakeParking,
						context
						);
				break;
			default:
				break;
			}
			
			OsmAgent leader = new OsmAgent(
					"1",
					agentBody,
					behavior
					);
			
			return Arrays.asList(leader);
		}
		
	}
	
	/*
	 * Parking at road junction test
	 */
	@Test
	public void testParkingBetweenTwoRoads() throws InterruptedException {
		SmartGov smartGov = loadSmartGov(TEST_MODE.TEST1);
		OsmAgentBody agent = (OsmAgentBody) smartGov.getContext().agents.get("1").getBody();
		Road road1 = ((OsmContext) smartGov.getContext()).roads.get("1");
		Road road2 = ((OsmContext) smartGov.getContext()).roads.get("2");
		
		assertThat(
				agent.getCurrentRoad(),
				equalTo(road1)
				);
		
		EventChecker enter = new EventChecker();
		agent.addOnParkingEnteredListener((event) -> {
			assertThat(
					road1.getForwardAgents().contains(agent),
					equalTo(false)
					);
			
			assertThat(
					road2.getForwardAgents().contains(agent),
					equalTo(false)
					);
			enter.check();
		});
		
		SmartGov.getRuntime().start(1000);
		
		SmartGov.getRuntime().waitUntilSimulatioEnd();
		
		assertThat(
				agent.getCurrentRoad(),
				equalTo(road2)
				);
		
		assertThat(
				enter.hasBeenTriggered(),
				equalTo(true)
				);
	}
	
	private static class Test1Context extends OsmContext {

		public Test1Context() {
			super(OsmAgentBodyTest.class.getResource("agent_body_test.properties").getFile());
		}
		
		@Override
		public Scenario loadScenario(String name) {
			return new TestScenario(TEST_MODE.TEST1);
		}
		
	}
	
	private static class TestBehavior1 extends MovingBehavior {
		
		private MoverAction nextAction;
		private boolean finalDestination = false;

		public TestBehavior1(MovingAgentBody agentBody, ParkingArea parking, SmartGovContext context) {
			super(
				agentBody,
				context.nodes.get("1"),
				context.nodes.get("2"),
				context);
			nextAction = MoverAction.ENTER(parking);
			
			agentBody.addOnDestinationReachedListener((event) -> {
				nextAction = MoverAction.ENTER(parking);
				if(finalDestination) {
					agentBody.getParkingEnteredListeners().clear();
					agentBody.addOnParkingEnteredListener((enterEvent) -> nextAction = MoverAction.WAIT());
				}
				else {
					refresh(getDestination(), context.nodes.get("3"));
					finalDestination = true;
				}
			});
			agentBody.addOnParkingEnteredListener((event) -> nextAction = MoverAction.LEAVE(parking));
			agentBody.addOnParkingLeftListener((event) -> nextAction = MoverAction.MOVE());

		}

		@Override
		public MoverAction provideAction() {
			return nextAction;
		}
		
	}
	
	/*
	 * Road junction as origin test
	 * 
	 * This test as been designed after a bug observation.
	 * 
	 * When we have 1<=>2<=>3, with roads 1<=>2 and 2<=>3,
	 * the road associated to node 2 is uncertain. But it was used
	 * to add agent to and remove agent from roads when entering / leaving
	 * parking areas, what caused errors. This has been solved.
	 *  
	 */
	@Test
	public void testOriginAtRoadJunction() throws InterruptedException {
		SmartGov smartGov = loadSmartGov(TEST_MODE.TEST2);
		OsmAgentBody agent = (OsmAgentBody) smartGov.getContext().agents.get("1").getBody();
		Road road1 = ((OsmContext) smartGov.getContext()).roads.get("1");
		Road road2 = ((OsmContext) smartGov.getContext()).roads.get("2");
		
		assertThat(
				agent.getCurrentRoad(),
				equalTo(road1)
				);
		
		
		
		EventChecker enter = new EventChecker();
		agent.addOnParkingEnteredListener((event) -> {
			assertThat(
					road1.getForwardAgents().contains(agent),
					equalTo(false)
					);
			
			assertThat(
					road2.getForwardAgents().contains(agent),
					equalTo(false)
					);
			enter.check();
		});
		
		SmartGov.getRuntime().start(1000);
		
		SmartGov.getRuntime().waitUntilSimulatioEnd();
		
		assertThat(
				agent.getCurrentRoad(),
				equalTo(road1)
				);
		
		assertThat(
				enter.hasBeenTriggered(),
				equalTo(true)
				);
	}
	
	private static class Test2Context extends OsmContext {

		public Test2Context() {
			super(OsmAgentBodyTest.class.getResource("agent_body_test.properties").getFile());
		}
		
		@Override
		public Scenario loadScenario(String name) {
			return new TestScenario(TEST_MODE.TEST2);
		}
		
	}
	
	private static class TestBehavior2 extends MovingBehavior {
		
		private MoverAction nextAction;

		public TestBehavior2(MovingAgentBody agentBody, ParkingArea parking, SmartGovContext context) {
			super(
				agentBody,
				context.nodes.get("2"),
				context.nodes.get("1"),
				context);
			nextAction = MoverAction.ENTER(parking);
			
			agentBody.addOnDestinationReachedListener((event) -> {
				nextAction = MoverAction.ENTER(parking);
				agentBody.getParkingEnteredListeners().clear();
				agentBody.addOnParkingEnteredListener((enterEvent) -> nextAction = MoverAction.WAIT());

			});
			agentBody.addOnParkingEnteredListener((event) -> nextAction = MoverAction.LEAVE(parking));
			agentBody.addOnParkingLeftListener((event) -> nextAction = MoverAction.MOVE());

		}

		@Override
		public MoverAction provideAction() {
			return nextAction;
		}
		
	}
}
