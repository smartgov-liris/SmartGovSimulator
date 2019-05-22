package smartgov.core.agent.events;

import smartgov.core.environment.graph.arc.Arc;

/**
 * Triggered each time an agent leave an Arc.
 * 
 * @author pbreugnot
 *
 */
public class ArcLeftEvent extends ArcEvent {

	public ArcLeftEvent(Arc<?> arc) {
		super(arc);
	}

}
