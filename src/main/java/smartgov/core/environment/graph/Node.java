package smartgov.core.environment.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.environment.graph.events.AgentOrigin;
import smartgov.core.environment.graph.events.AgentDestination;
import smartgov.core.events.EventHandler;
import smartgov.core.output.arc.ArcListIdSerializer;

/**
 * Node class.
 *
 * @author pbreugnot
 */
public class Node extends GraphItem {
	
	public String id;
	@JsonSerialize(using = ArcListIdSerializer.class)
	protected List<Arc> outgoingArcs;
	@JsonSerialize(using = ArcListIdSerializer.class)
	protected List<Arc> incomingArcs;

	@JsonIgnore
	private Collection<EventHandler<AgentOrigin>> agentOriginListeners;
	@JsonIgnore
	private Collection<EventHandler<AgentDestination>> agentDestinationListeners;
	
	/**
	 * Instanciate a Node without initial outgoing or incoming arcs.
	 *
	 * @param id Node id
	 */
	public Node(String id) {
		this(id, new ArrayList<>(), new ArrayList<>());
		this.agentOriginListeners = new ArrayList<>();
		this.agentDestinationListeners = new ArrayList<>();
	}

	/**
	 * Instanciate a Node with the specified outgoing and incoming arcs.
	 *
	 * @param id Node id
	 * @param outgoingArcs Outgoing Arcs
	 * @param incomingArcs Incoming Arcs	
	 */
	public Node(String id, List<Arc> outgoingArcs, List<Arc> incomingArcs) {
		this.id = id;
		this.outgoingArcs = outgoingArcs;
		this.incomingArcs = incomingArcs;
	}
	
	/**
	 * Node id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the incoming arcs list to the specified list.
	 *
	 * @param incomingArcs Incoming arcs
	 */
	public void setIncomingArcs(List<Arc> incomingArcs) {
		this.incomingArcs = incomingArcs;
	}
	
	/**
	 * Sets the outgoing arcs list to the specified list.
	 *
	 * @param outgoingArcs Outgoing arcs
	 */
	public void setOutgoingArcs(List<Arc> outgoingArcs) {
		this.outgoingArcs = outgoingArcs;
	}
	
	/**
	 * Adds the specified arc as an incoming arc.
	 *
	 * @param incomingArc Incoming arc to add
	 * @throws IllegalArgumentException if the arc is already in the incoming arcs list.
	 */
	public void addIncomingArc(Arc incomingArc){
		if(incomingArcs.contains(incomingArc)){
			throw new IllegalArgumentException("The specified arc is already registered as an incoming arc for this node.");
		}
		incomingArcs.add(incomingArc);
	}
	
	/**
	 * Adds the specified arc as an outgoing arc.
	 *
	 * @param outgoingArc Outgoing arc to add
	 * @throws IllegalArgumentException if the arc is already in the outgoing arcs list.
	 */
	public void addOutgoingArc(Arc outgoingArc){
		if(outgoingArcs.contains(outgoingArc)){
			throw new IllegalArgumentException("The specified arc is already registered as an outgoing arc for this node.");
		}
		outgoingArcs.add(outgoingArc);
	}
	
	/**
	 * Incoming arcs list.
	 */
	public List<Arc> getIncomingArcs() {
		return incomingArcs;
	}
	
	/**
	 * Outgoing arcs list.
	 */
	public List<Arc> getOutgoingArcs() {
		return outgoingArcs;
	}
	
	/**
	 * Adds an EventHandler for AgentOrigin events.
	 *
	 * Triggered when an agent reaches the node as the origin of its current plan.
	 */
	public void addAgentOriginListener(EventHandler<AgentOrigin> agentOriginListener) {
		agentOriginListeners.add(agentOriginListener);
	}
	
	/**
	 * Triggers registers handlers for the specified AgentOrigin event.
	 *
	 * @param event AgentOrigin event to handle
	 */
	public void triggerAgentOriginListeners(AgentOrigin event) {
		for (EventHandler<AgentOrigin> listener : agentOriginListeners) {
			listener.handle(event);
		}
	}
	
	/**
	 * Adds an EventHandler for AgentDestination events.
	 *
	 * Triggered when an agent reaches the node as the destination of its current plan.
	 */
	public void addAgentDestinationListener(EventHandler<AgentDestination> agentDestinationListener) {
		agentDestinationListeners.add(agentDestinationListener);
	}
	
	/**
	 * Triggers registers handlers for the specified AgentDestination event.
	 *
	 * @param event AgentDestination event to handle
	 */
	public void triggerAgentDestinationListeners(AgentDestination event) {
		for (EventHandler<AgentDestination> listener : agentDestinationListeners) {
			listener.handle(event);
		}
	}
}
