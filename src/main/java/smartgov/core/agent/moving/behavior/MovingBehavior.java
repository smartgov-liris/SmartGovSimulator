package smartgov.core.agent.moving.behavior;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.agent.core.behavior.Behavior;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.output.node.NodeIdSerializer;

public abstract class MovingBehavior extends Behavior<MoverAction> {
	@JsonIgnore
	private SmartGovContext context;
	
	@JsonSerialize(using=NodeIdSerializer.class)
	private Node origin;
	@JsonSerialize(using=NodeIdSerializer.class)
	private Node destination;
	
	public MovingBehavior(MovingAgentBody agentBody, Node origin, Node destination, SmartGovContext context) {
		super(agentBody);
		this.origin = origin;
		this.destination = destination;
		this.context = context;
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
		
		// Update agents plan
		((MovingAgentBody) getAgentBody()).updatePlan(context.graph.shortestPath(origin, destination));
	}

}
