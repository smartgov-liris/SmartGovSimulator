package org.liris.smartgov.simulator.core.agent.moving.events.arc;

import org.liris.smartgov.simulator.core.environment.graph.Arc;

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
