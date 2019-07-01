package smartgov.core.agent.moving.plan;

import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.events.Event;

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
