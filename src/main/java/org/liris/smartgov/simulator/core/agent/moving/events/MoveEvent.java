package org.liris.smartgov.simulator.core.agent.moving.events;

import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.events.Event;

/**
 * Event triggered each time a moving agent moves.
 *
 * More precisely, triggered when a MOVE action is
 * handled by the moving agent.
 *
 * @see org.liris.smartgov.simulator.core.agent.moving.MovingAgent
 * 
 * @author pbreugnot
 *
 */
public class MoveEvent extends Event {
	
	private Arc oldArc;
	private Arc newArc;
	private Node oldNode;
	private Node newNode;
	
	public MoveEvent(
			Arc oldArc,
			Arc newArc,
			Node oldNode,
			Node newNode) {
		super();
		this.oldArc = oldArc;
		this.newArc = newArc;
		this.oldNode = oldNode;
		this.newNode = newNode;
	}

	public Arc getOldArc() {
		return oldArc;
	}

	public Arc getNewArc() {
		return newArc;
	}

	public Node getOldNode() {
		return oldNode;
	}

	public Node getNewNode() {
		return newNode;
	}
	
	

}
