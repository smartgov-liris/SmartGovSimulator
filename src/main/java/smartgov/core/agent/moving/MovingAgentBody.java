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
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.events.EventHandler;

public abstract class MovingAgentBody extends AgentBody<MoverAction> {

	protected Plan plan;
	
	// Listeners collections for each event type
	protected Collection<EventHandler<MoveEvent>> agentMoveListeners;
	protected Collection<EventHandler<NodeReachedEvent>> nodeReachedListeners;
	protected Collection<EventHandler<ArcReachedEvent>> arcReachedListeners;
	protected Collection<EventHandler<ArcLeftEvent>> arcLeftListeners;
	protected Collection<EventHandler<DestinationReachedEvent>> destinationReachedListeners;
	
	public MovingAgentBody() {
		this.agentMoveListeners = new ArrayList<>();
		this.nodeReachedListeners = new ArrayList<>();
		this.arcReachedListeners = new ArrayList<>();
		this.arcLeftListeners = new ArrayList<>();
		this.destinationReachedListeners = new ArrayList<>();
	}
	
	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Plan getPlan() {
		return plan;
	}

	public void updatePlan(List<Node> nodes) {
		plan.update(nodes);
	}
	
	public void doAction(MoverAction action){
		// TODO : event listeners
		switch(action.getType()){
		case MOVE:
			Arc oldArc = plan.getCurrentArc();
			Node oldNode = plan.getCurrentNode();
			handleMove();
			triggerOnMoveListeners(new MoveEvent(
					oldArc,
					plan.getCurrentArc(),
					oldNode,
					plan.getCurrentNode()
					)
					);
			break;
		case ENTER:
			handleEnter(action.getParkingArea());
			break;
		case LEAVE:
			handleLeave(action.getParkingArea());
			break;
		default:
			handleWait();
		}
	}
	
	public abstract void handleMove();
	
	public abstract void handleEnter(ParkingArea parkingArea);
	
	public abstract void handleLeave(ParkingArea parkingArea);
	
	public abstract void handleWait();
	
	// Move listeners
	public void addOnMoveListener(EventHandler<MoveEvent> moveListener) {
		agentMoveListeners.add((EventHandler<MoveEvent>) moveListener);
	}
	
	public void triggerOnMoveListeners(MoveEvent event) {
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
	
	public void triggerNodeReachedListeners(NodeReachedEvent event) {
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
	
	public void triggerArcReachedListeners(ArcReachedEvent event) {
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
	
	public void triggerArcLeftListeners(ArcLeftEvent event) {
		for (EventHandler<ArcLeftEvent> eventHandler : arcLeftListeners) {
			eventHandler.handle(event);
		}
	}
	
	public Collection<EventHandler<ArcLeftEvent>> getArcLeftListeners() {
		return arcLeftListeners;
	}

	// Destination reached listeners
	public void addOnDestinationReachedListener(EventHandler<DestinationReachedEvent> nodeReachedListener) {
		destinationReachedListeners.add((EventHandler<DestinationReachedEvent>) nodeReachedListener);
	}
	
	public void triggerDestinationReachedListeners(DestinationReachedEvent event) {
		for (EventHandler<DestinationReachedEvent> eventHandler : destinationReachedListeners) {
			eventHandler.handle(event);
		}
	}

	public Collection<EventHandler<DestinationReachedEvent>> getDestinationReachedListeners() {
		return destinationReachedListeners;
	}
	
}
