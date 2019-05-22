package smartgov.core.agent.events;

import smartgov.core.environment.graph.arc.Arc;

/**
 * Triggered each time an agent reaches an Arc.
 * 
 * @author pbreugnot
 *
 */
public class ArcReachedEvent extends ArcEvent {

	public ArcReachedEvent(Arc<?> arc) {
		super(arc);
	}
	
}
