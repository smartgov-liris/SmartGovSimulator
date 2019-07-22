package smartgov.urban.osm.agent;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.ParkingArea;
import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.TestMovingBehavior;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Node;
import smartgov.core.scenario.Scenario;
import smartgov.urban.osm.agent.mover.CarMover;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.scenario.BasicOsmScenario;

/*
 * Here we will test the behavior of roads, parking areas and agents
 * in an osm graph.
 * 
 * When an osm agent enters a parking area, it should be removed from the
 * road, so that other agents can move freely.
 */
public class OsmParkingTest {
	
	private static SmartGov loadSmartGov() {
		return new SmartGov(new OsmParkingContext());
	}
	
	@Test
	public void testParkingAreaBehavior() throws InterruptedException {
		SmartGov smartGov = loadSmartGov();
		
		OsmAgentBody followerBody = (OsmAgentBody) smartGov.getContext().agents.get("2").getBody();
		OsmAgentBody leaderBody = (OsmAgentBody) smartGov.getContext().agents.get("1").getBody();
		
		EventChecker followerReachedNode3 = new EventChecker();
		EventChecker followerReachedNode4 = new EventChecker();
		EventChecker leaderLeftParkingArea = new EventChecker();
		EventChecker leaderReachedNode4 = new EventChecker();
		
		followerBody.addOnNodeReachedListener((event) -> {
				String nodeId = event.getNode().getId();
				if(nodeId.equals("3")) {
					// To be sure those assets are reached
					followerReachedNode3.check();
					
					// Assert that the leader is no more on the road
					assertThat(
							followerBody.getCurrentRoad().getForwardAgents(),
							contains(followerBody)
							);
					
					// Now, the follower has crossed node 3 (freely, because the leader
					// was no more on the road) and we now tell the former leader to 
					// leave the parking area so that it will spawn after the follower
					((OsmParkingBehavior) leaderBody.getAgent().getBehavior())
						.leaveParking();
				}
				else if (nodeId.equals("4")) {
					// To be sure those assets are reached
					followerReachedNode4.check();
					
					// The leader should have re-entered the road, after the follower
					assertThat(
							followerBody.getCurrentRoad().getForwardAgents(),
							contains(followerBody, leaderBody)
							);
					
					assertThat(
							followerBody.getCurrentRoad().leaderOfAgent(followerBody),
							nullValue()
							);
					assertThat(
							leaderBody.getCurrentRoad().leaderOfAgent(leaderBody),
							equalTo(followerBody)
							);
				}
			});
		
		leaderBody.addOnParkingLeftListener((event) -> {
			// To be sure those assets are reached
			leaderLeftParkingArea.check();
			
			// Checks that the plan has not been broken while the agent
			// was waiting on the parking area.
			// Notice that, while the agent was on the parking area, we
			// never changed its position.
			// It just disappeared from the road point of view.
			assertThat(
					leaderBody.getPlan().getCurrentNode().getId(),
					equalTo("3")
					);
			assertThat(
					leaderBody.getPlan().getLastNode().getId(),
					equalTo("4")
					);
		});
		
		leaderBody.addOnDestinationReachedListener((event) -> {
			// Finally checks that the leader reaches its destination,
			// and stops the simulation.
			assertThat(
					event.getNode().getId(),
					equalTo("4")
					);
			leaderReachedNode4.check();
			SmartGov.getRuntime().stop();
		});
		
		SmartGov.getRuntime().start(500);
		
		SmartGov.getRuntime().waitUntilSimulatioEnd();
		
		// To be sure that all the event based assert have been triggered
		assertThat(
				Arrays.asList(
					followerReachedNode3.hasBeenTriggered(),
					followerReachedNode4.hasBeenTriggered(),
					leaderLeftParkingArea.hasBeenTriggered(),
					leaderReachedNode4.hasBeenTriggered()),
				contains(true, true, true, true)
				);
	}
	
	private static class EventChecker {
		private Boolean triggered;
		
		public EventChecker() {
			this.triggered = false;
		}
		
		public void check() {
			this.triggered = true;
		}
		
		public Boolean hasBeenTriggered() {
			return triggered;
		}
	}
	
	

	/*
	 * Test scenario loader
	 */
	private static class OsmParkingContext extends OsmContext {

		public OsmParkingContext() {
			super(OsmParkingTest.class.getResource("osm_parking.properties").getFile());
		}
		
		@Override
		public Scenario loadScenario(String name) {
			return new OsmParkingScenario();
		}
		
	}
	
	/*
	 * Osm Scenario with 4 nodes, linked in a loop by a one way road (see mover/nodes.json
	 * and mover/ways.json in the corresponding resource directory).
	 * 
	 * 2 agents, called follower and leader, will live in the simulation.
	 * The follower spawns one node after the leader.
	 * The leader will stop in a parking area when it reaches node 3
	 */
	private static class OsmParkingScenario extends BasicOsmScenario {

		@Override
		public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
			/*
			 * Mock the ParkingArea interface : doesn't change anything because we
			 * are just interesting by events and agents behavior.
			 * We want to check if the agent correctly leaves the road and get back
			 * to it : we don't care about what it could do in a ParkingArea.
			 */
			ParkingArea fakeParking = mock(ParkingArea.class);
			
			/*
			 * Leader agent
			 */
			OsmAgentBody leaderBody = new OsmAgentBody(new CarMover(4, -6, 6, 8));
			OsmAgent leader = new OsmAgent(
					"1",
					leaderBody,
					new OsmParkingBehavior( // Custom parking behavior
							leaderBody,
							context.nodes.get("2"),
							context.nodes.get("4"),
							fakeParking,
							context
							)
					);

			/*
			 * Follower agent
			 */
			OsmAgentBody followerBody = new OsmAgentBody(new CarMover(4, -6, 6, 8));
			OsmAgent follower = new OsmAgent(
					"2",
					followerBody,
					new TestMovingBehavior( // Always the same shuttle behavior
							followerBody,
							context.nodes.get("1"),
							context.nodes.get("4"),
							context
							)
					);
			
			return Arrays.asList(leader, follower);
		}
		
	}
	
	/*
	 * Extends the shuttle behavior, but overrides the provide action method.
	 */
	private static class OsmParkingBehavior extends TestMovingBehavior {

		private MoverAction nextAction;
		private ParkingArea parkingArea;
		
		public OsmParkingBehavior(
				MovingAgentBody agentBody,
				Node origin,
				Node destination,
				ParkingArea parkingArea,
				SmartGovContext context) {
			super(agentBody, origin, destination, context);
			this.parkingArea = parkingArea;
			this.nextAction = MoverAction.MOVE();
			
			// Enters the parking area when node 3 is reached
			agentBody.addOnNodeReachedListener((event) -> {
				if (event.getNode().getId().equals("3"))
					nextAction = MoverAction.ENTER(parkingArea);
				});
			
			// Wait in the parking area
			agentBody.addOnParkingEnteredListener((event) -> nextAction = MoverAction.WAIT());
			
			// Move again when living the parking area
			agentBody.addOnParkingLeftListener((event) -> nextAction = MoverAction.MOVE());
		}
		
		/*
		 * Will be called externally to determine when the agent
		 * must leave the parking.
		 */
		public void leaveParking() {
			this.nextAction = MoverAction.LEAVE(parkingArea);
		}

		@Override
		public MoverAction provideAction() {
			return nextAction;
		}
		
	}
}
