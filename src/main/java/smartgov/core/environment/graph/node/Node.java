package smartgov.core.environment.graph.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.environment.graph.GraphItem;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.events.AgentOrigin;
import smartgov.core.environment.graph.events.AgentDestination;
import smartgov.core.events.EventHandler;
import smartgov.core.output.arc.ArcListIdSerializer;

/**
 * Generic Node class.
 * 
 * @see Arc
 * @author pbreugnot
 *
 * @param <T> Associated Arc type
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
	
	public Node(String id) {
		this(id, new ArrayList<>(), new ArrayList<>());
		this.agentOriginListeners = new ArrayList<>();
		this.agentDestinationListeners = new ArrayList<>();
	}
	
	public Node(String id, List<Arc> outgoingArcs, List<Arc> incomingArcs) {
		this.id = id;
		this.outgoingArcs = outgoingArcs;
		this.incomingArcs = incomingArcs;
	}
	
	public String getId() {
		return id;
	}
	
	public void setIncomingArcs(List<Arc> incomingArcs) {
		this.incomingArcs = incomingArcs;
	}
	
	public void setOutgoingArcs(List<Arc> outgoingArcs) {
		this.outgoingArcs = outgoingArcs;
	}
	
	public void addIncomingArc(Arc incomingArc){
		if(!incomingArcs.contains(incomingArc)){
			incomingArcs.add(incomingArc);
		}
	}
	
	public void addOutgoingArc(Arc outgoingArc){
		if(!outgoingArcs.contains(outgoingArc)){
			outgoingArcs.add(outgoingArc);
		}
	}
	
	public List<Arc> getIncomingArcs() {
		return incomingArcs;
	}
	
	public List<Arc> getOutgoingArcs() {
		return outgoingArcs;
	}
	
	public void addAgentOriginListener(EventHandler<AgentOrigin> agentOriginListener) {
		agentOriginListeners.add(agentOriginListener);
	}
	
	public void triggerAgentOriginListeners(AgentOrigin event) {
		for (EventHandler<AgentOrigin> listener : agentOriginListeners) {
			listener.handle(event);
		}
	}
	
	public void addAgentDestinationListener(EventHandler<AgentDestination> agentDestinationListener) {
		agentDestinationListeners.add(agentDestinationListener);
	}
	
	public void triggerAgentDestinationListeners(AgentDestination event) {
		for (EventHandler<AgentDestination> listener : agentDestinationListeners) {
			listener.handle(event);
		}
	}
}
