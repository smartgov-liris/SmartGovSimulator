package smartgov.core.environment.graph.events;

import smartgov.core.agent.AbstractAgent;

public class SinkAgentEvent extends AgentArrival {

	public SinkAgentEvent(AbstractAgent<?> agent) {
		super(agent);
	}

}
