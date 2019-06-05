package smartgov.core.agent.moving.behavior;

import smartgov.core.agent.core.behavior.AgentAction;
import smartgov.core.agent.moving.ParkingArea;

/**
 * Action allows agents to interact with the environment.
 * The detail of the action is reserved by the actionable object that describes
 * what it can do with the specific action.
 * @see ActionableByHumanAgent
 * @author spageaud
 *
 */
public class MoverAction extends AgentAction{
	
	public enum ActionType {
		WAIT(0),
		MOVE(1),
		ENTER(2),
		LEAVE(3);
	
		private final int index;
		
		ActionType(int index){
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	}
	
	private ActionType type;
	private ParkingArea parkingArea;
	
	private MoverAction(ActionType type, ParkingArea parkingArea) {
		this.type = type;
		this.parkingArea = parkingArea;
	}

	public ActionType getType() {
		return type;
	}

	public ParkingArea getParkingArea() {
		return parkingArea;
	}
	
	public static MoverAction MOVE() {
		return new MoverAction(ActionType.MOVE, null);
	}
	
	public static MoverAction WAIT() {
		return new MoverAction(ActionType.WAIT, null);
	}
	
	public static MoverAction ENTER(ParkingArea parkingArea) {
		return new MoverAction(ActionType.MOVE, parkingArea);
	}
	
	public static MoverAction LEAVE(ParkingArea parkingArea) {
		return new MoverAction(ActionType.LEAVE, parkingArea);
	}
}
