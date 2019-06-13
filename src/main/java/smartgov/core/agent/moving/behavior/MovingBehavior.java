package smartgov.core.agent.moving.behavior;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.agent.core.behavior.Behavior;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Node;
import smartgov.core.output.node.NodeIdSerializer;

/**
 * An abstract Behavior implementation for agents moving in the simulation.
 *
 * <p>
 * Such behavior is defined by an <em>origin</em> and a <em>destination</em> between
 * which the agents associated to this behavior should move.
 * </p>
 *
 * <p>
 * Notice that, from a model point of view, the agent should be given a MovingBehavior
 * once, at instantiation time, or if it needs to be <b>completely</b> reinirilized, aka
 * if its behavior change.
 * </p>
 *
 * <p>
 * When the Agent has reached its destination, it is more consistent to {@link #refresh refresh()} 
 * its behavior than associating a new MovingBehavior to it.
 * </p>
 *
 * <p>
 * The {@link #provideAction} function will provide only {@link MoverAction}s. 
 * </p>
 */
public abstract class MovingBehavior extends Behavior<MoverAction> {
	@JsonIgnore
	private SmartGovContext context;
	
	@JsonSerialize(using=NodeIdSerializer.class)
	private Node origin;
	@JsonSerialize(using=NodeIdSerializer.class)
	private Node destination;
	
	/**
	 * MovingBehavior constructor.
	 *
	 * @param agentBody AgentBody of the Agent to which this Behavior will be associated.
	 * In practice, this reference is used by {@link #updateAgentBodyPlan}.
	 * @param origin Initial origin
	 * @param destination Initiali destination
	 * @param context Current context. Used by {@link #updateAgentBodyPlan} to compute the new AgentBody's Plan.
	 */
	
	/*
	 * Notice that the agent body's plan is not yet updated : {@link #updateAgentBodyPlan} should be called
	 * later, once the agent body <b>AND</b> its agent have been instanciated. Typically, this is done in
	 * the MovingAgent constructor.
	 */
	public MovingBehavior(MovingAgentBody agentBody, Node origin, Node destination, SmartGovContext context) {
		super(agentBody);
		this.origin = origin;
		this.destination = destination;
		this.context = context;
	}

	/**
	 * Current origin.
	 */
	public Node getOrigin() {
		return origin;
	}

	/**
	 * Current destination.
	 */
	public Node getDestination() {
		return destination;
	}

	/**
	 * Current context.
	 */	
	public SmartGovContext getContext() {
		return context;
	}
	
	/**
	 * Updates the AgentBody's Plan according to the current origin and destination.
	 *
	 * <p>
	 * Computes the shortest path from origin to destination, and update the AgentBody's Plan
	 * with the computed nodes.
	 * </p>
	 *
	 * <p>
	 * The Behavior doesn't directly have a reference to the current Plan, because the behavior
	 * actually just represents the <i>will</i> of the agent to go from the origin to the destination.
	 * </p>
	 *
	 * <p>
	 * So it might look inconsistent to update the AgentBody's Plan from the Behavior : the Plan should be
	 * updated externally, according to the Behavior. This is completely true, however this is done there
	 * because it is easier for the user to automatically update the Plan each time the {@link #refresh #refresh()} method
	 * is called. Otherwise, one could either forget to update the AgentBody's Plan after a behavior refresh,
	 * or, worse, externally compute a Plan with origin and destination that doesn't correspond to the behavior.
	 * </p>
	 */
	public void updateAgentBodyPlan() {
		((MovingAgentBody) getAgentBody()).updatePlan(context.getGraph().shortestPath(origin, destination));
	}

	/**
	 * Updates the current behavior with the specified origin and destination.
	 *
	 * Also updates the AgentBody's Plan accordingly, with the shortest path from
	 * origin to destination.
	 */
	public void refresh(Node origin, Node destination) {
		this.origin = origin;
		this.destination = destination;
		
		// Update agents plan
		updateAgentBodyPlan();
	}

}
