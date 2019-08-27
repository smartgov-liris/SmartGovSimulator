package org.liris.smartgov.simulator.core.agent.moving.plan;

import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.events.Event;

public class FirstNodeEvent extends Event {

	private Node firstNode;
	private Arc firstArc;

	public FirstNodeEvent(Node firstNode, Arc firstArc) {
		super();
		this.firstNode = firstNode;
		this.firstArc = firstArc;
	}

	public Node getFirstNode() {
		return firstNode;
	}
	public Arc getFirstArc() {
		return firstArc;
	}
	
}
