package smartgov.core.environment.graph.events;

import smartgov.core.agent.AbstractAgent;

/**
 * Event triggered when an agent spawn on a SourceNode.
 * 
 * @author pbreugnot
 *
 */
public class SpawnAgentEvent extends AgentEvent {

	public SpawnAgentEvent(AbstractAgent agent) {
		super(agent);
	}
	
}
