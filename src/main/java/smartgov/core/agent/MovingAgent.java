package smartgov.core.agent;

import smartgov.core.agent.behavior.MoverAction;
import smartgov.core.agent.behavior.MovingBehavior;

public class MovingAgent extends Agent<MoverAction>{

	public MovingAgent(String id, MovingAgentBody body, MovingBehavior behavior) {
		super(id, body, behavior);
		body.setPlan(new Plan(this));
	}

}
