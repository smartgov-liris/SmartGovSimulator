package org.liris.smartgov.simulator.core.environment.graph.events;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgent;

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
