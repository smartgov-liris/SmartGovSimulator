package smartgov.core.agent.moving.behavior;

/**
 * A Movable object can handle {@link MoverAction MoverAction}s.
 */
public interface Movable {

	/**
	 * Handles and performs the specified MoverAction.
	 *
	 * @param action action to perform
	 */
	public void doAction(MoverAction action);
	
}
