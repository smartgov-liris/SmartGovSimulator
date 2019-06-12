package smartgov.core.agent.moving;

public class TestMovingAgentBody extends MovingAgentBody {

	@Override
	public void handleMove() {
		plan.reachANode();
	}

	@Override
	public void handleEnter(ParkingArea parkingArea) {
		
	}

	@Override
	public void handleLeave(ParkingArea parkingArea) {
		
	}

	@Override
	public void handleWait() {
		
	}

	@Override
	public void handleWander() {
		// TODO Auto-generated method stub
		
	}

}
