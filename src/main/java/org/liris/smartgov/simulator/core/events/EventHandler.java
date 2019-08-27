package org.liris.smartgov.simulator.core.events;

/**
 * Generic event handler interface.
 * 
 * @author pbreugnot
 *
 * @param <E> Event to handle.
 */
public interface EventHandler<E extends Event> {

	public void handle(E event);
	
}
