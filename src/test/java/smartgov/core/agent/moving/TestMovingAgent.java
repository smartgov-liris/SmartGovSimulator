package smartgov.core.agent.moving;

import smartgov.core.agent.moving.behavior.TestMovingBehavior;

public class TestMovingAgent extends MovingAgent {
	
	public TestMovingAgent(String id, TestMovingAgentBody body, TestMovingBehavior behavior) {
		super(id, body, behavior);
	}

}
