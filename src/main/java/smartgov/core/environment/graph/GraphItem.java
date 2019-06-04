package smartgov.core.environment.graph;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.environment.graph.events.AgentDeparture;
import smartgov.core.events.EventHandler;

/**
 * Super class for nodes and arcs.
 * 
 * Define listeners for common events of nodes and arcs, such as agent arrival and departures.
 * 
 * @author pbreugnot
 *
 */
public abstract class GraphItem {
	
	@JsonIgnore
	private Collection<EventHandler<AgentArrival>> agentArrivalListeners;
	@JsonIgnore
	private Collection<EventHandler<AgentDeparture>> agentDepartureListeners;
	
	public GraphItem() {
		agentArrivalListeners = new ArrayList<>();
		agentDepartureListeners = new ArrayList<>();
	}
	
	/*
	 * Agent Arrival
	 */
	public void addAgentArrivalListener(EventHandler<AgentArrival> listener) {
		agentArrivalListeners.add(listener);
	}
	
	public void triggerAgentArrivalListeners(AgentArrival event) {
		for (EventHandler<AgentArrival> listener : agentArrivalListeners) {
			listener.handle(event);
		}
	}

	public Collection<EventHandler<AgentArrival>> getAgentArrivalListeners() {
		return agentArrivalListeners;
	}
	
	/*
	 * Agent Departure
	 */
	public void addAgentDepartureListener(EventHandler<AgentDeparture> listener) {
		agentDepartureListeners.add(listener);
	}
	
	public void triggerAgentDepartureListeners(AgentDeparture event) {
		for (EventHandler<AgentDeparture> listener : agentDepartureListeners) {
			listener.handle(event);
		}
	}

	public Collection<EventHandler<AgentDeparture>> getAgentDepartureListeners() {
		return agentDepartureListeners;
	}
}
