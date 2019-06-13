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
		this.plan = new Plan();
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
	 * Also triggers <em>origin</em>, <em>node reached</em> and <em>arc reached</em>
	 * events on the first node and arc of the updated plan.
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
		// TODO : other event listeners
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

	/**
	 * Automatically called to perform a {@link smartgov.core.agent.moving.behavior.MoverAction#MOVE MOVE} action.
	 */	
	public abstract void handleMove();

	/**
	 * Automatically called to perform a {@link smartgov.core.agent.moving.behavior.MoverAction#WAIT WAIT} action.
	 */	
	public abstract void handleWait();

	/**
	 * Automatically called to perform a {@link smartgov.core.agent.moving.behavior.MoverAction#WANDER WANDER} action.
	 */	
	public abstract void handleWander();

	/**
	 * Automatically called to perform a {@link smartgov.core.agent.moving.behavior.MoverAction#ENTER ENTER} action
	 * in the specified ParkingArea.
	 */	
	public abstract void handleEnter(ParkingArea parkingArea);

	/**
	 * Automatically called to perform a {@link smartgov.core.agent.moving.behavior.MoverAction#LEAVE LEAVE} action
	 * in the specified ParkingArea.
	 */	
	public abstract void handleLeave(ParkingArea parkingArea);
	
	// Move listeners
	/**
	 * Adds a new handler for MoveEvents.
	 *
	 * Triggered each time a MOVE action is performed, just after {@link #handleMove} is called.
	 */
	public void addOnMoveListener(EventHandler<MoveEvent> moveListener) {
		agentMoveListeners.add((EventHandler<MoveEvent>) moveListener);
	}
	
	private void triggerOnMoveListeners(MoveEvent event) {
		for (EventHandler<MoveEvent> eventHandler : agentMoveListeners) {
			eventHandler.handle(event);
		}
	}

	/**
	 * EventHandlers for MoveEvents.
	 */	
	public Collection<EventHandler<MoveEvent>> getAgentMoveListeners() {
		return agentMoveListeners;
	}

	// Node reached listeners
	/**
	 * Adds a new handler for NodeReachedEvents.
	 *
	 * <p> 
	 * Triggered each time the agent cross a node, or stop on it. Also triggered when the
	 * agent "reach" the origin node that correspond to its behavior. In consequence, in the
	 * case of an agent that reach its node destination and has this same node as origin for
	 * its new plan, listeners will be called twice on the same node : when the agent reach the
	 * node as its destination, and again when it reaches it as its origin (even if, technically,
	 * it has not moved).
	 * </p>
	 *
	 * <p>
	 * If what you want to implement is behaviors at origin and / or destination, please also consider
	 * {@link #addOnOriginReachedListener addOnOriginReachedListener} and
	 * {@link #addOnDestinationReachedListener addOnDestinationReachedListener}.
	 * </p>
	 */
	public void addOnNodeReachedListener(EventHandler<NodeReachedEvent> nodeReachedListener) {
		nodeReachedListeners.add((EventHandler<NodeReachedEvent>) nodeReachedListener);
	}
	
	private void triggerNodeReachedListeners(NodeReachedEvent event) {
		for (EventHandler<NodeReachedEvent> eventHandler : nodeReachedListeners) {
			eventHandler.handle(event);
		}
	}
	
	/**
	 * EventHandlers for NodeReachedEvents.
	 */
	public Collection<EventHandler<NodeReachedEvent>> getNodeReachedListeners() {
		return nodeReachedListeners;
	}

	// Arc reached listeners
	/**
	 * Adds a new handler for ArcReachedEvent.
	 *
	 * Triggered each time an agent reaches a new Arc.
	 */
	public void addOnArcReachedListener(EventHandler<ArcReachedEvent> arcReachedListener) {
		arcReachedListeners.add((EventHandler<ArcReachedEvent>) arcReachedListener);
	}
	
	private void triggerArcReachedListeners(ArcReachedEvent event) {
		for (EventHandler<ArcReachedEvent> eventHandler : arcReachedListeners) {
			eventHandler.handle(event);
		}
	}
	
	/**
	 * EventHandlers for ArcReachedEvents.
	 */
	public Collection<EventHandler<ArcReachedEvent>> getArcReachedListeners() {
		return arcReachedListeners;
	}

	// Arc left listeners
	/**
	 * Adds a new handler for ArcLeftEvents.
	 *
	 * Called each time an agent leave an Arc.
	 */
	public void addOnArcLeftListener(EventHandler<ArcLeftEvent> arcLeftListener) {
		arcLeftListeners.add((EventHandler<ArcLeftEvent>) arcLeftListener);
	}
	
	private void triggerArcLeftListeners(ArcLeftEvent event) {
		for (EventHandler<ArcLeftEvent> eventHandler : arcLeftListeners) {
			eventHandler.handle(event);
		}
	}

	/**
	 * EventHandlers for ArcLeftEvents.
	 */	
	public Collection<EventHandler<ArcLeftEvent>> getArcLeftListeners() {
		return arcLeftListeners;
	}

	// Destination reached listeners
	/**
	 * Adds a new handler for OriginReachedEvent.
	 *
	 * Triggered at the very first step of the agent Plan, when {@link #updatePlan updatePlan()} is called,
	 * on the origin node of its {@link smartgov.core.agent.moving.behavior.MovingBehavior}. 
	 */
	public void addOnOriginReachedListener(EventHandler<OriginReachedEvent> originReachedListener) {
		originReachedListeners.add((EventHandler<OriginReachedEvent>) originReachedListener);
	}

	/**
	 * EventHandlers for OriginReachedEvents.
	 */	
	private void triggerOriginReachedListeners(OriginReachedEvent event) {
		for (EventHandler<OriginReachedEvent> eventHandler : originReachedListeners) {
			eventHandler.handle(event);
		}
	}
		
	// Destination reached listeners
	/**
	 * Adds a new handler for DestinationReached.
	 *
	 * Triggered when the agent has reached the last node of its current {@link Plan}, that corresponds
	 * to the destination node of its {@link smartgov.core.agent.moving.behavior.MovingBehavior}.
	 */
	public void addOnDestinationReachedListener(EventHandler<DestinationReachedEvent> destinationReachedListener) {
		destinationReachedListeners.add((EventHandler<DestinationReachedEvent>) destinationReachedListener);
	}
	
	private void triggerDestinationReachedListeners(DestinationReachedEvent event) {
		for (EventHandler<DestinationReachedEvent> eventHandler : destinationReachedListeners) {
			eventHandler.handle(event);
		}
	}

	/**
	 * EventHandlers for DestinationReachedEvents.
	 */
	public Collection<EventHandler<DestinationReachedEvent>> getDestinationReachedListeners() {
		return destinationReachedListeners;
	}
	
}
