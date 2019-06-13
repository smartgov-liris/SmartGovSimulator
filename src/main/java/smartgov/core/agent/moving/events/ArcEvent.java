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

	public ArcEvent(Arc arc) {
		super();
		this.arc = arc;
	}
	
	/**
	 * Arc involved in the Event.
	 */
	public Arc getArc() {
		return arc;
	}
}
