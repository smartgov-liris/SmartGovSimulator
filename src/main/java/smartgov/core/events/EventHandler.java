package smartgov.core.events;

/**
 * General event handler interface.
 * 
 * @author pbreugnot
 *
 * @param <E> Event to handle.
 */
public interface EventHandler<E extends Event> {

	public void handle(E event);
	
}
