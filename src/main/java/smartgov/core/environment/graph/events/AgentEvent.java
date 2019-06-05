package smartgov.core.environment.graph.events;

import smartgov.core.agent.moving.MovingAgent;
import smartgov.core.events.Event;

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
