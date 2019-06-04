package smartgov.core.agent;

import smartgov.core.agent.behavior.AbstractBehavior;
import smartgov.core.agent.behavior.MoverAction;

public class MovingAgent extends Agent<MoverAction>{

	public MovingAgent(String id, AgentBody<MoverAction> body, AbstractBehavior<MoverAction> behavior) {
		super(id, body, behavior);
	}

}
