package smartgov.core.agent.moving;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import smartgov.core.agent.core.AgentBody;
import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.events.ArcLeftEvent;
import smartgov.core.agent.moving.events.ArcReachedEvent;
import smartgov.core.agent.moving.events.DestinationReachedEvent;
import smartgov.core.agent.moving.events.MoveEvent;
import smartgov.core.agent.moving.events.NodeReachedEvent;
import smartgov.core.agent.moving.events.OriginReachedEvent;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.environment.graph.events.AgentDeparture;
import smartgov.core.environment.graph.events.AgentDestination;
import smartgov.core.environment.graph.events.AgentOrigin;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.events.EventHandler;

/**
 * Abstract implementation of an AgentBody to represent agents moving in the simulation.
 * <p>
 * Handles MoverActions thanks to the dedicated functions to implement.
 * </p>
 *
 * <p>
 * Also provides lots of events relative to the MovingBehavior and move actions in general,
 * useful to make the simulation more dynamic.
 * </p>
 */
public abstract class MovingAgentBody extends AgentBody<MoverAction> {

	private Plan plan;
	
	// Listeners collections for each event type
	private Collection<EventHandler<MoveEvent>> agentMoveListeners;
	private Collection<EventHandler<NodeReachedEvent>> nodeReachedListeners;
	private Collection<EventHandler<ArcReachedEvent>> arcReachedListeners;
	private Collection<EventHandler<ArcLeftEvent>> arcLeftListeners;
	private Collection<EventHandler<OriginReachedEvent>> originReachedListeners;
	private Collection<EventHandler<DestinationReachedEvent>> destinationReachedListeners;

	/**
	 * MovingAgentBody constructor.
	 */	
	public MovingAgentBody() {
		this.agentMoveListeners = new ArrayList<>();
		this.nodeReachedListeners = new ArrayList<>();
		this.arcReachedListeners = new ArrayList<>();
		this.arcLeftListeners = new ArrayList<>();
		this.originReachedListeners = new ArrayList<>();
		this.destinationReachedListeners = new ArrayList<>();
	}

	/**
	 * Set the plan of this agent.
	 */	
	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	/**
	 * Plan of this agent.
	 */
	public Plan getPlan() {
		return plan;
	}

	/**
	 * Update the plan of this agent body with the specified nodes.
	 *
	 * <p>
	 * All the nodes of the plan will be replaced, from origin to destination.
	 * </p>
	 *
	 * <p>
	 * Actually just an alias for <code>this.getPlan().update(nodes)</code>
	 * </p>
	 *
	 * @see Plan#update
	 */
	public void updatePlan(List<Node> nodes) {
		plan.update(nodes);
		// Node reached events
		triggerNodeReachedListeners(new NodeReachedEvent(plan.getCurrentNode()));
		plan.getCurrentNode().triggerAgentArrivalListeners(new AgentArrival((MovingAgent) getAgent()));
		
		// Origin event
		plan.getCurrentNode().triggerAgentOriginListeners(new AgentOrigin((MovingAgent) getAgent()));
		triggerOriginReachedListeners(new OriginReachedEvent(plan.getCurrentNode()));
		
		// Arc reached events
		triggerArcReachedListeners(new ArcReachedEvent(plan.getCurrentArc()));
		plan.getCurrentArc().triggerAgentArrivalListeners(new AgentArrival((MovingAgent) getAgent()));
	}

	/**
	 * Handles the specified MoverActions, thanks to the implemented <code>handle</code>
	 * functions of this class.
	 *
	 * <p>
	 * Also triggers available events accordingly.
	 * </p>
	 */	
	public void doAction(MoverAction action){
		// TODO : event listeners
		switch(action.getType()){
		case MOVE:
			Arc oldArc = plan.getCurrentArc();
			Node oldNode = plan.getCurrentNode();
			handleMove();
			Arc newArc = plan.getCurrentArc();
			Node newNode = plan.getCurrentNode();
			buildAndTriggerEventsAtMove(oldArc, oldNode, newArc, newNode);
			
			break;
		case WAIT:
			handleWait();
			break;
		case ENTER:
			handleEnter(action.getParkingArea());
			break;
		case LEAVE:
			handleLeave(action.getParkingArea());
			break;
		default:
			handleWander();
		}
	}
	
	private void buildAndTriggerEventsAtMove(Arc oldArc, Node oldNode, Arc newArc, Node newNode) {
		// Move event, always triggered
		triggerOnMoveListeners(
			new MoveEvent(
				oldArc,
				newArc,
				oldNode,
				newNode
				)
			);
		
		// Arc relative events
		if(oldArc != newArc) {
			oldArc.triggerAgentDepartureListeners(new AgentDeparture((MovingAgent) getAgent()));
			triggerArcLeftListeners(new ArcLeftEvent(oldArc));
			if(newArc != null) {
				newArc.triggerAgentArrivalListeners(new AgentArrival((MovingAgent) getAgent()));
				triggerArcReachedListeners(new ArcReachedEvent(newArc));
			}
		}
		
		// Node relative events
		if(oldNode != newNode) {
			oldNode.triggerAgentDepartureListeners(new AgentDeparture((MovingAgent) getAgent()));
			if(newNode != null) {
				newNode.triggerAgentArrivalListeners(new AgentArrival((MovingAgent) getAgent()));
				triggerNodeReachedListeners(new NodeReachedEvent(newNode));
			}
		}
		
		// Has the agent reached its destination?
		if(plan.isPathComplete()) {
			/*
			 * When the agent has reached its destination, there is no next arc, so oldArc = newArc,
			 * but we still need to notify that the last arc has been left.
			 */
			oldArc.triggerAgentDepartureListeners(new AgentDeparture((MovingAgent) getAgent()));
			triggerArcLeftListeners(new ArcLeftEvent(oldArc));
			
			/*
			 * Destination reached listeners
			 */
			newNode.triggerAgentDestinationListeners(new AgentDestination((MovingAgent) getAgent()));
			triggerDestinationReachedListeners(new DestinationReachedEvent(newNode));
		}
	}
	
	public abstract void handleMove();
	
	public abstract void handleWait();
	
	public abstract void handleWander();
	
	public abstract void handleEnter(ParkingArea parkingArea);
	
	public abstract void handleLeave(ParkingArea parkingArea);
	
	// Move listeners
	public void addOnMoveListener(EventHandler<MoveEvent> moveListener) {
		agentMoveListeners.add((EventHandler<MoveEvent>) moveListener);
	}
	
	private void triggerOnMoveListeners(MoveEvent event) {
		for (EventHandler<MoveEvent> eventHandler : agentMoveListeners) {
			eventHandler.handle(event);
		}
	}
	
	public Collection<EventHandler<MoveEvent>> getAgentMoveListeners() {
		return agentMoveListeners;
	}

	// Node reached listeners
	public void addOnNodeReachedListener(EventHandler<NodeReachedEvent> nodeReachedListener) {
		nodeReachedListeners.add((EventHandler<NodeReachedEvent>) nodeReachedListener);
	}
	
	private void triggerNodeReachedListeners(NodeReachedEvent event) {
		for (EventHandler<NodeReachedEvent> eventHandler : nodeReachedListeners) {
			eventHandler.handle(event);
		}
	}
	
	public Collection<EventHandler<NodeReachedEvent>> getNodeReachedListeners() {
		return nodeReachedListeners;
	}

	// Arc reached listeners
	public void addOnArcReachedListener(EventHandler<ArcReachedEvent> arcReachedListener) {
		arcReachedListeners.add((EventHandler<ArcReachedEvent>) arcReachedListener);
	}
	
	private void triggerArcReachedListeners(ArcReachedEvent event) {
		for (EventHandler<ArcReachedEvent> eventHandler : arcReachedListeners) {
			eventHandler.handle(event);
		}
	}
	
	public Collection<EventHandler<ArcReachedEvent>> getArcReachedListeners() {
		return arcReachedListeners;
	}

	// Arc left listeners
	public void addOnArcLeftListener(EventHandler<ArcLeftEvent> arcLeftListener) {
		arcLeftListeners.add((EventHandler<ArcLeftEvent>) arcLeftListener);
	}
	
	private void triggerArcLeftListeners(ArcLeftEvent event) {
		for (EventHandler<ArcLeftEvent> eventHandler : arcLeftListeners) {
			eventHandler.handle(event);
		}
	}
	
	public Collection<EventHandler<ArcLeftEvent>> getArcLeftListeners() {
		return arcLeftListeners;
	}

	// Destination reached listeners
	public void addOnOriginReachedListener(EventHandler<OriginReachedEvent> nodeReachedListener) {
		originReachedListeners.add((EventHandler<OriginReachedEvent>) nodeReachedListener);
	}
	
	private void triggerOriginReachedListeners(OriginReachedEvent event) {
		for (EventHandler<OriginReachedEvent> eventHandler : originReachedListeners) {
			eventHandler.handle(event);
		}
	}
		
	// Destination reached listeners
	public void addOnDestinationReachedListener(EventHandler<DestinationReachedEvent> nodeReachedListener) {
		destinationReachedListeners.add((EventHandler<DestinationReachedEvent>) nodeReachedListener);
	}
	
	private void triggerDestinationReachedListeners(DestinationReachedEvent event) {
		for (EventHandler<DestinationReachedEvent> eventHandler : destinationReachedListeners) {
			eventHandler.handle(event);
		}
	}

	public Collection<EventHandler<DestinationReachedEvent>> getDestinationReachedListeners() {
		return destinationReachedListeners;
	}
	
}
