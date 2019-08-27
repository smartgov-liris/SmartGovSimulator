package org.liris.smartgov.simulator.core.agent.moving.plan;

import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.events.Event;

public class NextNodeEvent extends Event {
	
	private Arc oldArc;
	private Arc newArc;
	private Node oldNode;
	private Node newNode;
	
	public NextNodeEvent(
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
