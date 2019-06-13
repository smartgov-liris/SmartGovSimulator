package smartgov.core.agent.moving.events.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.TestMovingAgent;
import smartgov.core.agent.moving.TestMovingAgentBody;
import smartgov.core.agent.moving.behavior.TestMovingBehavior;
import smartgov.core.agent.moving.events.ArcLeftEvent;
import smartgov.core.agent.moving.events.ArcReachedEvent;
import smartgov.core.agent.moving.events.DestinationReachedEvent;
import smartgov.core.agent.moving.events.MoveEvent;
import smartgov.core.agent.moving.events.NodeReachedEvent;
import smartgov.core.agent.moving.events.OriginReachedEvent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.TestContext;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.environment.graph.events.AgentDeparture;
import smartgov.core.environment.graph.events.AgentDestination;
import smartgov.core.environment.graph.events.AgentOrigin;
import smartgov.core.events.EventHandler;
import smartgov.core.simulation.TestScenario;

public class TestEventsScenario extends TestScenario {
	
	public static final String name = "TestEvents";
	
	// Arc reached
	public List<String> arcReachedIds = new ArrayList<>();
	public List<String> arcAgentArrivalTriggeredIds = new ArrayList<>();
	
	// Arc left
	public List<String> arcLeftIds = new ArrayList<>();
	public List<String> arcAgentDepartureTriggeredIds = new ArrayList<>();
	
	// Node reached
	public List<String> nodeReachedIds = new ArrayList<>();
	public List<String> nodeAgentArrivalTriggeredIds = new ArrayList<>();
	
	// Destination reached
	public List<String> originReachedIds = new ArrayList<>();
	public List<String> nodeAgentOriginTriggeredIds = new ArrayList<>();
		
	// Destination reached
	public List<String> destinationReachedIds = new ArrayList<>();
	public List<String> nodeAgentDestinationTriggeredIds = new ArrayList<>();
	
	// Move events
	public List<MoveEvent> moveEvents = new ArrayList<>();

	@Override
	public Collection<Agent> buildAgents(SmartGovContext context) {
		// New body for the first shuttle
		TestMovingAgentBody shuttleBody = new TestMovingAgentBody();
		
		/*
		 * All event listeners are set up before the Agent instantiation,
		 * because first events are triggered during Agent instantiation.
		 * (when the first updateAgentBodyPlan is called)
		 */
		// Arc reached
		setUpArcReachedListeners(shuttleBody);
		setUpArcAgentArrivalListeners(context.arcs.values());
		
		// Arc left
		setUpArcLeftListeners(shuttleBody);
		setUpArcAgentDepartureListeners(context.arcs.values());
		
		// Node reached
		setUpNodeReachedListeners(shuttleBody);
		setUpNodeAgentArrivalListeners(context.nodes.values());
		
		// Destination reached
		setUpOriginReachedListeners(shuttleBody);
		setUpNodeAgentOriginListeners(context.nodes.values());
				
		// Destination reached
		setUpDestinationReachedListeners(shuttleBody);
		setUpNodeAgentDestinationListeners(context.nodes.values());
		
		// Move event
		setUpMoveListeners(shuttleBody);
		
		// The first shuttle will perform round trips between nodes 1 and 5.
		TestMovingAgent shuttle1 = new TestMovingAgent(
				"1",
				shuttleBody,
				new TestMovingBehavior(
						shuttleBody,
						context.nodes.get("1"),
						context.nodes.get("5"),
						(TestContext) context)
				);

		return Arrays.asList(shuttle1);
	}
	
	/*
	 * Arc reached
	 */
	private void setUpArcReachedListeners(MovingAgentBody shuttleBody) {
		shuttleBody.addOnArcReachedListener(new EventHandler<ArcReachedEvent>() {

			@Override
			public void handle(ArcReachedEvent event) {
				arcReachedIds.add(event.getArc().getId());
			}
			
		});
	}
	
	private void setUpArcAgentArrivalListeners(Collection<Arc> arcs) {
		for(Arc arc : arcs) {
			arc.addAgentArrivalListener(new EventHandler<AgentArrival>() {

				@Override
				public void handle(AgentArrival event) {
					arcAgentArrivalTriggeredIds.add(arc.getId());
				}
				
			});
		}
	}
	
	/*
	 * Arc left
	 */
	private void setUpArcLeftListeners(MovingAgentBody shuttleBody) {
		shuttleBody.addOnArcLeftListener(new EventHandler<ArcLeftEvent>() {

			@Override
			public void handle(ArcLeftEvent event) {
				arcLeftIds.add(event.getArc().getId());
			}
			
		});
	}
	
	private void setUpArcAgentDepartureListeners(Collection<Arc> arcs) {
		for(Arc arc : arcs) {
			arc.addAgentDepartureListener(new EventHandler<AgentDeparture>() {

				@Override
				public void handle(AgentDeparture event) {
					arcAgentDepartureTriggeredIds.add(arc.getId());
				}
				
			});
		}
	}
	
	/*
	 * Node reached
	 */
	private void setUpNodeReachedListeners(MovingAgentBody shuttleBody) {
		shuttleBody.addOnNodeReachedListener(new EventHandler<NodeReachedEvent>() {

			@Override
			public void handle(NodeReachedEvent event) {
				nodeReachedIds.add(event.getNode().getId());
			}
			
		});
	}
	
	private void setUpNodeAgentArrivalListeners(Collection<Node> nodes) {
		for(Node node : nodes) {
			node.addAgentArrivalListener(new EventHandler<AgentArrival>() {

				@Override
				public void handle(AgentArrival event) {
					nodeAgentArrivalTriggeredIds.add(node.getId());
				}
				
			});
		}
	}
	
	/*
	 * Origin reached
	 */
	private void setUpOriginReachedListeners(MovingAgentBody shuttleBody) {
		shuttleBody.addOnOriginReachedListener(new EventHandler<OriginReachedEvent>() {

			@Override
			public void handle(OriginReachedEvent event) {
				originReachedIds.add(event.getNode().getId());
			}
			
		});
	}
	
	private void setUpNodeAgentOriginListeners(Collection<Node> nodes) {
		for(Node node : nodes) {
			node.addAgentOriginListener(new EventHandler<AgentOrigin>() {

				@Override
				public void handle(AgentOrigin event) {
					nodeAgentOriginTriggeredIds.add(node.getId());
				}
				
			});
		}
	}
	
	/*
	 * Destination reached
	 */
	private void setUpDestinationReachedListeners(MovingAgentBody shuttleBody) {
		shuttleBody.addOnDestinationReachedListener(new EventHandler<DestinationReachedEvent>() {

			@Override
			public void handle(DestinationReachedEvent event) {
				destinationReachedIds.add(event.getNode().getId());
			}
			
		});
	}
	
	private void setUpNodeAgentDestinationListeners(Collection<Node> nodes) {
		for(Node node : nodes) {
			node.addAgentDestinationListener(new EventHandler<AgentDestination>() {

				@Override
				public void handle(AgentDestination event) {
					nodeAgentDestinationTriggeredIds.add(node.getId());
				}
				
			});
		}
	}
	
	/*
	 * Move
	 */
	private void setUpMoveListeners(MovingAgentBody shuttleBody) {
		shuttleBody.addOnMoveListener(new EventHandler<MoveEvent>() {

			@Override
			public void handle(MoveEvent event) {
				moveEvents.add(event);
			}
			
		});
	}
}
