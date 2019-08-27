package org.liris.smartgov.simulator.core.environment.graph.events;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgent;

/**
 * An event triggered when a new agent arrives on an Arc or a Node.
 * 
 * @author pbreugnot
 *
 */
public class AgentArrival extends AgentEvent {

	public AgentArrival(MovingAgent agent) {
		super(agent);
	}

}
