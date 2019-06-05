package smartgov.core.environment.graph.events;

import smartgov.core.agent.moving.MovingAgent;

/**
 * Event triggered when an agent spawn on a SourceNode.
 * 
 * @author pbreugnot
 *
 */
public class AgentDestination extends AgentEvent {

	public AgentDestination(MovingAgent agent) {
		super(agent);
	}
	
}
