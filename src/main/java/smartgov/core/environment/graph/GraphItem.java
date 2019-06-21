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
 * Convenient super class to define listeners for common events of nodes and arcs,
 * such as agent arrival and departures.
 * 
 * @author pbreugnot
 *
 */
public abstract class GraphItem {
	
	@JsonIgnore
	private Collection<EventHandler<AgentArrival>> agentArrivalListeners;
	@JsonIgnore
	private Collection<EventHandler<AgentDeparture>> agentDepartureListeners;
	
	/**
	 * GraphItem constructor.
	 */
	public GraphItem() {
		agentArrivalListeners = new ArrayList<>();
		agentDepartureListeners = new ArrayList<>();
	}
	
	// Agent Arrival
	/**
	 * Adds an EventHandler for AgentArrival events.
	 *
	 * Triggered when an agent reach the item.
	 *
	 * @param listener new event handler to add
	 */
	public void addAgentArrivalListener(EventHandler<AgentArrival> listener) {
		agentArrivalListeners.add(listener);
	}

	/**
	 * Triggers registered listeners of AgentArrival events.
	 *
	 * @param event AgentArrival event to handle
	 */	
	public void triggerAgentArrivalListeners(AgentArrival event) {
		for (EventHandler<AgentArrival> listener : agentArrivalListeners) {
			listener.handle(event);
		}
	}

	/**
	 * EventHandlers for AgentArrival events.
	 *
	 * @return current agent arrival event listeners
	 */
	public Collection<EventHandler<AgentArrival>> getAgentArrivalListeners() {
		return agentArrivalListeners;
	}
	
	// Agent Departure
	/**
	 * Adds an EventHandler for AgentDeparture events.
	 *
	 * Triggered when an agent leave the item.
	 *
	 * @param listener new event handler to add
	 */
	public void addAgentDepartureListener(EventHandler<AgentDeparture> listener) {
		agentDepartureListeners.add(listener);
	}
	
	/**
	 * Triggers registered listeners of AgentDeparture events.
	 *
	 * @param event AgentDeparture event to handle
	 */	
	public void triggerAgentDepartureListeners(AgentDeparture event) {
		for (EventHandler<AgentDeparture> listener : agentDepartureListeners) {
			listener.handle(event);
		}
	}

	/**
	 * EventHandlers for AgentDeparture events.
	 *
	 * @return current agent departure event listeners
	 */
	public Collection<EventHandler<AgentDeparture>> getAgentDepartureListeners() {
		return agentDepartureListeners;
	}
}
