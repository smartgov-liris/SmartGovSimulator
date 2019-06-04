package smartgov.core.agent.behavior;

import smartgov.core.agent.AgentBody;

public abstract class MovingBehavior extends Behavior<MoverAction> {

	public MovingBehavior(AgentBody<MoverAction> agentBody) {
		super(agentBody);
	}

}
