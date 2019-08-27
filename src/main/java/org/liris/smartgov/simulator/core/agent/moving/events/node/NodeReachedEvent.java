package org.liris.smartgov.simulator.core.agent.moving.events.node;

import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.events.Event;

/**
 * Event triggered when an agent reach a node.
 *
 * Reaching a node doesn't mean that the agent really stopped on this node,
 * but that he has at least crossed the node.
 * 
 * @author pbreugnot
 *
 */
public class NodeReachedEvent extends Event {
	
	private Node node;
	
	public NodeReachedEvent(Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}

}
