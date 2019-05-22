package smartgov.core.agent.events;

import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.events.Event;

/**
 * Event implying an Arc.
 * 
 * @author pbreugnot
 *
 */
public abstract class ArcEvent extends Event {
	
	private Arc<?> arc;

	public ArcEvent(Arc<?> arc) {
		super();
		this.arc = arc;
	}

	public Arc<?> getArc() {
		return arc;
	}
}
