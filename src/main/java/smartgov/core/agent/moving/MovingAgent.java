package smartgov.core.agent.moving;

import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.MovingBehavior;

public class MovingAgent extends Agent<MoverAction>{

	public MovingAgent(String id, MovingAgentBody body, MovingBehavior behavior) {
		super(id, body, behavior);
		body.setPlan(new Plan(this));
		behavior.updateAgentBodyPlan();
	}

}
