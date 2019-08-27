package org.liris.smartgov.simulator.core.environment.graph.events;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgent;
import org.liris.smartgov.simulator.core.events.Event;

/**
 * General class to describe an event involving an Agent.
 * 
 * @author pbreugnot
 *
 */
public abstract class AgentEvent extends Event {

	private MovingAgent agent;
	
	public AgentEvent(MovingAgent agent) {
		this.agent = agent;
	}
	
	public MovingAgent getAgent() {
		return agent;
	}
}
