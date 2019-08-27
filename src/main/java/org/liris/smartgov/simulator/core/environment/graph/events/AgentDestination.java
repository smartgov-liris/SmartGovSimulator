package org.liris.smartgov.simulator.core.environment.graph.events;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgent;

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
