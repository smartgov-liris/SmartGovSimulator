package smartgov.core.agent.moving;

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
