package smartgov.core.agent.moving.plan;

import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.events.Event;

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
