package org.liris.smartgov.simulator.core.agent.moving;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgent;
import org.liris.smartgov.simulator.core.agent.moving.behavior.TestMovingBehavior;

public class TestMovingAgent extends MovingAgent {
	
	public TestMovingAgent(String id, TestMovingAgentBody body, TestMovingBehavior behavior) {
		super(id, body, behavior);
	}

}
