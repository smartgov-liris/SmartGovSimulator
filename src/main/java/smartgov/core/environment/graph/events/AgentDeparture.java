package smartgov.core.environment.graph.events;

import smartgov.core.agent.MovingAgent;

/**
 * Event triggered when an agent leave an Arc or a Node.
 * 
 * @author pbreugnot
 *
 */
public class AgentDeparture extends AgentEvent {

	public AgentDeparture(MovingAgent agent) {
		super(agent);
	}
	
}
