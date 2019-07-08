package smartgov.core.simulation.time;

/**
 * A functional interface to trigger an action at a specific time.
 * 
 *
 */
public interface DelayedAction {
	
	/**
	 * Action to perform.
	 */
	public abstract void trigger();

}
