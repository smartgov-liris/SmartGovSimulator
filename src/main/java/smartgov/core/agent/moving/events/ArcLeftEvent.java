package smartgov.core.agent.moving.events;

import smartgov.core.environment.graph.Arc;

/**
 * Triggered each time an agent leave an Arc.
 * 
 * @author pbreugnot
 *
 */
public class ArcLeftEvent extends ArcEvent {

	public ArcLeftEvent(Arc arc) {
		super(arc);
	}

}
