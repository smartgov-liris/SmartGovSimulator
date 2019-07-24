package smartgov.urban.geo.agent.behavior;

import org.graphstream.algorithm.AStar.Costs;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Node;
import smartgov.urban.geo.environment.graph.DistanceCosts;

public abstract class GeoMovingBehavior extends MovingBehavior {

	/**
	 * GeoMovingBehavior constructor.
	 *
	 * @param agentBody AgentBody of the Agent to which this Behavior will be associated.
	 * In practice, this reference is used by {@link #updateAgentBodyPlan}.
	 * @param origin Initial origin
	 * @param destination Initial destination
	 * @param context Current context. Used by {@link #updateAgentBodyPlan} to compute the new AgentBody's Plan.
	 * @param costs AStar costs used to compute the shortest path between origin and destination
	 */
	public GeoMovingBehavior(MovingAgentBody agentBody, Node origin, Node destination, SmartGovContext context,
			Costs costs) {
		super(agentBody, origin, destination, context, costs);
	}
	
	/**
	 * GeoMovingBehavior constructor.
	 * 
	 * The default costs used are {@link smartgov.urban.geo.environment.graph.DistanceCosts} instantiated from
	 * the current context nodes. This should be quite safe, because normally this function is called when building
	 * agents, and <i>buildAgents</i> is called after <i>buildNodes</i> and <i>buildArcs</i>. However, in special cases,
	 * you can manually pass a custom cost using the other constructor. 
	 *
	 * @param agentBody AgentBody of the Agent to which this Behavior will be associated.
	 * In practice, this reference is used by {@link #updateAgentBodyPlan}.
	 * @param origin Initial origin
	 * @param destination Initial destination
	 * @param context Current context. Used by {@link #updateAgentBodyPlan} to compute the new AgentBody's Plan.
	 */
	public GeoMovingBehavior(MovingAgentBody agentBody, Node origin, Node destination, SmartGovContext context) {
		super(agentBody, origin, destination, context, new DistanceCosts(context.nodes, context.arcs));
	}

}
