package org.liris.smartgov.simulator.core.agent.moving.events.arc;

import org.liris.smartgov.simulator.core.environment.graph.Arc;

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
