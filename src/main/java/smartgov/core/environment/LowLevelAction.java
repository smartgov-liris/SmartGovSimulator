package smartgov.core.environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Action allows agents to interact with the environment.
 * The detail of the action is reserved by the actionable object that describes
 * what it can do with the specific action.
 * @see ActionableByHumanAgent
 * @author spageaud
 *
 */
public enum LowLevelAction {
	
	IDLE(0),
	MOVE(1),
	ENTER(2),
	LEAVE(3),
	MOVETO(4);

	private final int index;
	
	LowLevelAction(int index){
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
}
