package org.liris.smartgov.simulator.core.agent.moving;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.agent.moving.MovingAgent;
import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.agent.moving.ParkingArea;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MoverAction;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MovingBehavior;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.scenario.Scenario;

public class TestParkingArea {
	
	private static SmartGov loadSmartGov() {
		return new SmartGov(new ParkingContext());
	}
	
	/*
	 * Test scenario set up
	 */
	@Test
	public void loadScenario() {
		SmartGov smartGov = loadSmartGov();
		
		assertThat(
				smartGov.getContext().nodes.values(),
				hasSize(3)
				);
		
		assertThat(
				smartGov.getContext().arcs.values(),
				hasSize(2)
				);
		
		assertThat(
				smartGov.getContext().agents.values(),
				hasSize(1)
				);
		
		assertThat(
				ParkingScenario.fakeParkingArea,
				notNullValue()
				);
		
		assertThat(
				ParkingScenario.spyParkingBody,
				notNullValue()
				);
	}
	
	@Test
	public void testParkingBehavior() throws InterruptedException {
		loadSmartGov();
		
		SmartGov.getRuntime().start(20);
		
		SmartGov.getRuntime().waitUntilSimulatioEnd();
		
		// Enter 1 time
		verify(ParkingScenario.spyParkingBody, times(1)).handleEnter(ParkingScenario.fakeParkingArea);
		verify(ParkingScenario.fakeParkingArea, times(1)).enter(any(MovingAgent.class));
		
		// Leave 1 time
		verify(ParkingScenario.spyParkingBody, times(1)).handleLeave(ParkingScenario.fakeParkingArea);
		verify(ParkingScenario.fakeParkingArea, times(1)).leave(any(MovingAgent.class));
		
		// Wait 10 iterations in the parking
		verify(ParkingScenario.spyParkingBody, times(10)).handleWait();
	}
	
	private static class ParkingContext extends SmartGovContext {

		public ParkingContext() {
			super(TestParkingArea.class.getResource("parking_config.properties").getFile());
		}
		
		@Override
		public Scenario loadScenario(String name) {
			return new ParkingScenario();
		}
		
	}

	/*
	 * Scenario with only 3 nodes : 0 -> 1 -> 2
	 */
	private static class ParkingScenario extends Scenario {
		
		public static ParkingAgentBody spyParkingBody;
		public static ParkingArea fakeParkingArea;

		@Override
		public Collection<? extends Node> buildNodes(SmartGovContext context) {
			Collection<Node> nodes = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				nodes.add(new Node(String.valueOf(i)));
			}
			return nodes;
		}

		@Override
		public Collection<? extends Arc> buildArcs(SmartGovContext context) {
			Collection<Arc> arcs = new ArrayList<>();
			arcs.add(new Arc("1", context.nodes.get("0"), context.nodes.get("1"), 1));
			arcs.add(new Arc("2", context.nodes.get("1"), context.nodes.get("2"), 1));
			return arcs;
		}

		@Override
		public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
			Collection<MovingAgent> agents = new ArrayList<>();
			
			/*
			 * Mock the ParkingArea interface : we just want to check
			 * if enter and leave are called properly.
			 */
			fakeParkingArea = mock(ParkingArea.class);
			
			/*
			 * Spies the agent body, to be able to count actions
			 * performed.
			 */
			ParkingAgentBody agentBody = new ParkingAgentBody();
			spyParkingBody = spy(agentBody);
			
			/*
			 * A behavior to move from node 0 to 2, and stop in
			 * a parking area at node 1.
			 */
			ParkingBehavior parkingBehavior = new ParkingBehavior(
					spyParkingBody,
					fakeParkingArea,
					context
					);
			
			agents.add(new MovingAgent(
					"1",
					spyParkingBody,
					parkingBehavior
					)
			);
			
			return agents;
		}
		
	}
	
	/*
	 * Very basic agent body, just go the the next node.
	 */
	private static class ParkingAgentBody extends MovingAgentBody {

		@Override
		public void handleMove() {
			getPlan().reachNextNode();
		}

		@Override
		public void handleWait() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void handleWander() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/*
	 * Behavior to :
	 * - move until node 0
	 * - enter the parking area
	 * - wait 10 ticks
	 * - leave the parking area
	 * - move until the destination (node 2)
	 * - wander until the end of the simulation
	 */
	private static class ParkingBehavior extends MovingBehavior {
		
		private ParkingArea parkingArea;
		private int waitCounter = 0;
		private boolean parkingReached = false;
		private boolean inParking = false;

		public ParkingBehavior(
				MovingAgentBody agentBody,
				ParkingArea parkingArea,
				SmartGovContext context) {
			super(agentBody, context.nodes.get("0"), context.nodes.get("2"), context);
			this.parkingArea = parkingArea;
		}

		/*
		 * This is a quite dirty and inefficient implementation that has been 
		 * designed for the sake of example.
		 * 
		 * In a real use case, it is much better to use an event driven approach
		 * to modify the internal state of the agent behavior.
		 */
		@Override
		public MoverAction provideAction() {
			if(!inParking) {
				if (!parkingReached && ((MovingAgentBody) getAgentBody()).getPlan().getCurrentNode().getId().equals("1")) {
					parkingReached = true;
					inParking = true;
					return MoverAction.ENTER(parkingArea);
				}
				
				if (!((MovingAgentBody) getAgentBody()).getPlan().isComplete()) {
					return MoverAction.MOVE();
				}
				return MoverAction.WANDER();
			}
			
			if (waitCounter < 10) {
				waitCounter++;
				return MoverAction.WAIT();
			}
			waitCounter = 0;
			inParking = false;
			return MoverAction.LEAVE(parkingArea);
			
		}
		
	}
}
