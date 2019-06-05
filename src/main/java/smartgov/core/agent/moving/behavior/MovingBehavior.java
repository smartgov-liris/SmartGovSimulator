package smartgov.core.agent.moving.behavior;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.agent.core.AgentBody;
import smartgov.core.agent.core.behavior.Behavior;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.output.node.NodeIdSerializer;

public abstract class MovingBehavior extends Behavior<MoverAction> {

	@JsonSerialize(using=NodeIdSerializer.class)
	private Node origin;
	@JsonSerialize(using=NodeIdSerializer.class)
	private Node destination;
	
	public MovingBehavior(AgentBody<MoverAction> agentBody, Node origin, Node destination) {
		super(agentBody);
		this.origin = origin;
		this.destination = destination;
	}

	public Node getOrigin() {
		return origin;
	}

	public Node getDestination() {
		return destination;
	}
	
	public void refresh(Node origin, Node destination) {
		this.origin = origin;
		this.destination = destination;
	}

}
