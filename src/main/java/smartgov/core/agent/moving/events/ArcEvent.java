package smartgov.core.agent.moving.events;

import smartgov.core.environment.graph.Arc;
import smartgov.core.events.Event;

/**
 * Event implying an Arc.
 * 
 * @author pbreugnot
 *
 */
public abstract class ArcEvent extends Event {
	
	private Arc arc;

	/**
	 * ArcEvent constructor.
	 *
	 * @param arc arc involved in this event
	 */
	public ArcEvent(Arc arc) {
		super();
		this.arc = arc;
	}
	
	/**
	 * Arc involved in the Event.
	 *
	 * @return arc involved in this event
	 */
	public Arc getArc() {
		return arc;
	}
}
