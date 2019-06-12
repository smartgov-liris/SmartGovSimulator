package smartgov.core.agent.moving.behavior;

import smartgov.core.agent.core.behavior.AgentAction;
import smartgov.core.agent.moving.ParkingArea;

/**
 * AgentAction implementation that defines common actions for agents
 * that physically move in the simulation.
 *
 * Espacially useful to describe low level agents in an urban context.
 * (eg: Buses that move between spots, human agents looking for a parking area...)
 *
 * @author spageaud, pbreugnot
 *
 */
public class MoverAction extends AgentAction{
	
	/**
	 * Available actions types that correspond to MoverActions.
	 */
	public enum ActionType {
		WAIT,
		MOVE,
		WANDER,
		ENTER,
		LEAVE;
	}
	
	private ActionType type;
	private ParkingArea parkingArea;
	
	private MoverAction(ActionType type, ParkingArea parkingArea) {
		this.type = type;
		this.parkingArea = parkingArea;
	}

	/**
	 * Type of this action.
	 *
	 * Should be used in a <code>switch</code> statement to handle
	 * different actions.
	 *
	 */
	public ActionType getType() {
		return type;
	}

	/**
	 * ParkingArea concerned by <code>ENTER</code> and <code>LEAVE</code> actions.
	 *
	 * <code>null</code> for <code>WAIT</code>, <code>MOVE</code> and <code>WANDER</code> actions.
	 */
	public ParkingArea getParkingArea() {
		return parkingArea;
	}
	
	/**
	 * A <code>MOVE</code> action.
	 */
	public static MoverAction MOVE() {
		return new MoverAction(ActionType.MOVE, null);
	}

	/**
	 * A <code>WAIT</code> action.
	 */
	public static MoverAction WAIT() {
		return new MoverAction(ActionType.WAIT, null);
	}
	
	/**
	 * A <code>WANDER</code> action.
	 */
	public static MoverAction WANDER() {
		return new MoverAction(ActionType.WANDER, null);
	}

	/**
	 * An <code>ENTER</code> action in the specified ParkingArea.
	 *
	 * @param parkingArea the ParkingArea to enter in.
	 */
	public static MoverAction ENTER(ParkingArea parkingArea) {
		return new MoverAction(ActionType.MOVE, parkingArea);
	}

	/**
	 * An <code>LEAVE</code> action from the specified ParkingArea.
	 *
	 * @param parkingArea the ParkingArea to leave from.
	 */
	public static MoverAction LEAVE(ParkingArea parkingArea) {
		return new MoverAction(ActionType.LEAVE, parkingArea);
	}
}
