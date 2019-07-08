package smartgov.core.agent.moving.events.arc;

import smartgov.core.environment.graph.Arc;

/**
 * Triggered each time an agent reaches an Arc.
 * 
 * @author pbreugnot
 *
 */
public class ArcReachedEvent extends ArcEvent {

	public ArcReachedEvent(Arc arc) {
		super(arc);
	}
	
}
