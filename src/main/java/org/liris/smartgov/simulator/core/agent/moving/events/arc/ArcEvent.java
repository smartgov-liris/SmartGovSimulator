package org.liris.smartgov.simulator.core.agent.moving.events.arc;

import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.events.Event;

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
