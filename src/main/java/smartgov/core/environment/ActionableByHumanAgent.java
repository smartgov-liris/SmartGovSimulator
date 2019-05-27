package smartgov.core.environment;

import java.util.List;

/**
 * Implementing this interface allows an object to be the target of a specific Action.
 * An actionable object gives a list of available actions one can do to it.
 * 
 * @see LowLevelAction
 * @author spageaud
 *
 */
public interface ActionableByHumanAgent {

	List<LowLevelAction> getAvailableHumanActions();
	
	void doHumanAction(LowLevelAction action);
}
