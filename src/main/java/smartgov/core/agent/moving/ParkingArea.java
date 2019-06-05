package smartgov.core.agent.moving;

public interface ParkingArea {

	public void enter(MovingAgent agent);
	public void leave(MovingAgent agent);
	public int spaceLeft();

}
