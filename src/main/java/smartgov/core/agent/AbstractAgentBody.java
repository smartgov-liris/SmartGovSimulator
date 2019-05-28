package smartgov.core.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import smartgov.core.agent.events.ArcLeftEvent;
import smartgov.core.agent.events.ArcReachedEvent;
import smartgov.core.agent.events.DestinationReachedEvent;
import smartgov.core.agent.events.MoveEvent;
import smartgov.core.agent.events.NodeReachedEvent;
import smartgov.core.environment.LowLevelAction;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.events.EventHandler;

/**
 * An agent body represents its physical part, that is distinct from its "mind".
 * @see AbstractAgent
 * 
 * @author pbreugnot
 *
 */

public abstract class AbstractAgentBody<Tagent extends AbstractAgent<?>, Tnode extends Node<Tarc>, Tarc extends Arc<Tnode>> {

	protected Plan<Tnode, Tarc> plan;
	protected Tagent agent;
	
	// Listeners collections for each event type
	protected Collection<EventHandler<MoveEvent>> agentMoveListeners;
	protected Collection<EventHandler<NodeReachedEvent>> nodeReachedListeners;
	protected Collection<EventHandler<ArcReachedEvent>> arcReachedListeners;
	protected Collection<EventHandler<ArcLeftEvent>> arcLeftListeners;
	protected Collection<EventHandler<DestinationReachedEvent>> destinationReachedListeners;
	
	public AbstractAgentBody() {
		this.agentMoveListeners = new ArrayList<>();
		this.nodeReachedListeners = new ArrayList<>();
		this.arcReachedListeners = new ArrayList<>();
		this.arcLeftListeners = new ArrayList<>();
		this.destinationReachedListeners = new ArrayList<>();
	}
	
	public abstract void initialize();
	
	public void setAgent(Tagent agent) {
		this.agent = agent;
		plan.setAgent(agent);
	}
	
	public Tagent getAgent() {
		return agent;
	}

	public Plan<Tnode, Tarc> getPlan() {
		return plan;
	}

	public void updatePlan(List<Tnode> nodes) {
		plan.update(nodes);
	}
	
	public void doAction(LowLevelAction action){
		// TODO : event listeners
		switch(action){
		case MOVE:
			triggerOnMoveListeners(move());
			break;
		case ENTER:
			enter();
			break;
		case LEAVE:
			leave();
			break;
		case MOVETO:
			moveTo();
			break;
		default:
			idle();
		}
	}
	
	public abstract MoveEvent move();
	
	public abstract void enter();
	
	public abstract void leave();
	
	public abstract void moveTo();
	
	public abstract void idle();
	
	// Move listeners
	public void addOnMoveListener(EventHandler<? extends MoveEvent> moveListener) {
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
	public void addOnNodeReachedListener(EventHandler<? extends NodeReachedEvent> nodeReachedListener) {
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
	public void addOnArcReachedListener(EventHandler<? extends ArcReachedEvent> arcReachedListener) {
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
	public void addOnArcLeftListener(EventHandler<? extends ArcLeftEvent> arcLeftListener) {
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
	public void addOnDestinationReachedListener(EventHandler<? extends NodeReachedEvent> nodeReachedListener) {
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
