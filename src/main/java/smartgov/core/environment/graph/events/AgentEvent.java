package smartgov.core.environment.graph.events;

import smartgov.core.agent.AbstractAgent;
import smartgov.core.events.Event;

/**
 * General class to describe an event involving an Agent.
 * 
 * @author pbreugnot
 *
 */
public abstract class AgentEvent extends Event {

	private AbstractAgent<?> agent;
	
	public AgentEvent(AbstractAgent<?> agent) {
		this.agent = agent;
	}
	
	public AbstractAgent<?> getAgent() {
		return agent;
	}
}
