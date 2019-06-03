package smartgov.core.environment.graph.events;

import smartgov.core.agent.AbstractAgent;

/**
 * An event triggered when a new agent arrives on an Arc or a Node.
 * 
 * @author pbreugnot
 *
 */
public class AgentArrival extends AgentEvent {

	public AgentArrival(AbstractAgent agent) {
		super(agent);
	}

}
