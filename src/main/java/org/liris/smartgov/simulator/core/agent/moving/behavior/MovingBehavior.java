package org.liris.smartgov.simulator.core.agent.moving.behavior;

import org.liris.smartgov.simulator.core.agent.core.behavior.Behavior;
import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.environment.graph.DefaultLengthCosts;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.environment.graph.astar.Costs;
import org.liris.smartgov.simulator.core.output.node.NodeIdSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
 * once, at instantiation time, or if it needs to be <b>completely</b> reinitialized, aka
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
	
	@JsonIgnore
	private Costs costs;
	

	/**
	 * MovingBehavior constructor. Uses distances as default costs to refresh the behavior.
	 * 
	 * <p>
	 * Notice that this default cost is quite inefficient, because it doesn't use any heuristic,
	 * even if it uses the generic arc length.
	 * </p>
	 *
	 * @param agentBody AgentBody of the Agent to which this Behavior will be associated.
	 * In practice, this reference is used by {@link #updateAgentBodyPlan}.
	 * @param origin Initial origin
	 * @param destination Initial destination
	 * @param context Current context. Used by {@link #updateAgentBodyPlan} to compute the new AgentBody's Plan.
	 */
	public MovingBehavior(MovingAgentBody agentBody, Node origin, Node destination, SmartGovContext context) {
		this(agentBody, origin, destination, context, new DefaultLengthCosts());
	}
	
	/**
	 * MovingBehavior constructor.
	 *
	 * @param agentBody AgentBody of the Agent to which this Behavior will be associated.
	 * In practice, this reference is used by {@link #updateAgentBodyPlan}.
	 * @param origin Initial origin
	 * @param destination Initial destination
	 * @param context Current context. Used by {@link #updateAgentBodyPlan} to compute the new AgentBody's Plan.
	 * @param costs AStar costs used to compute the shortest path between origin and destination
	 */
	
	/*
	 * Notice that the agent body's plan is not yet updated : {@link #updateAgentBodyPlan} should be called
	 * later, once the agent body <b>AND</b> its agent have been instantiated. Typically, this is done in
	 * the MovingAgent constructor.
	 */
	public MovingBehavior(MovingAgentBody agentBody, Node origin, Node destination, SmartGovContext context, Costs costs) {
		super(agentBody);
		this.origin = origin;
		this.destination = destination;
		this.context = context;
		this.costs = costs;
	}

	/**
	 * Current origin.
	 *
	 * @return origin of this behavior
	 */
	public Node getOrigin() {
		return origin;
	}

	/**
	 * Current destination.
	 *
	 * @return destination of this behavior
	 */
	public Node getDestination() {
		return destination;
	}

	/**
	 * Current context.
	 *
	 * @return associated smartGov context
	 */	
	public SmartGovContext getContext() {
		return context;
	}
	
	/**
	 * Returns the currents costs used by the AStar algorithm
	 * to compute shortest paths when refreshing the behavior
	 * from one node to an other.
	 * 
	 * @return AStar costs
	 */
	public Costs getCosts() {
		return costs;
	}
	
	/**
	 * Sets the costs used by the AStar algorithm
	 * to compute shortest paths when refreshing the behavior
	 * from one node to an other.
	 * 
	 * @param costs AStar costs
	 */
	public void setCosts(Costs costs) {
		this.costs = costs;
	}

	/**
	 * Updates the AgentBody's Plan according to the current origin and destination.
	 *
	 * <p>
	 * Computes the shortest path from origin to destination, and update the AgentBody's Plan
	 * with the computed nodes, using the costs specified in the constructor, set by {@link #setCosts}
	 * or the default distances costs if no costs were provided.
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
		((MovingAgentBody) getAgentBody()).updatePlan(context.getGraph().shortestPath(origin, destination, costs));
	}

	/**
	 * Updates the current behavior with the specified origin and destination.
	 *
	 * Also updates the AgentBody's Plan accordingly, with the shortest path from
	 * origin to destination.
	 *
	 * @param origin new origin
	 * @param destination new destination
	 */
	public void refresh(Node origin, Node destination) {
		this.origin = origin;
		this.destination = destination;
		
		// Update agents plan
		updateAgentBodyPlan();
	}

}
