package org.liris.smartgov.simulator.core.agent.moving;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;

public class TestMovingAgentBody extends MovingAgentBody {

	@Override
	public void handleMove() {
		getPlan().reachNextNode();
	}

	@Override
	public void handleWait() {
		
	}

	@Override
	public void handleWander() {
		// TODO Auto-generated method stub
		
	}

}
