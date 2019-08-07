package smartgov.urban.geo.agent.behavior;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Node;
import smartgov.core.environment.graph.astar.Costs;
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
	 * The default costs used are {@link smartgov.urban.geo.environment.graph.DistanceCosts}.
	 * <p>
	 * You can also manually pass a custom cost function using the other constructor. 
	 * </p>
	 * 
	 * @param agentBody AgentBody of the Agent to which this Behavior will be associated.
	 * In practice, this reference is used by {@link #updateAgentBodyPlan}.
	 * @param origin Initial origin
	 * @param destination Initial destination
	 * @param context Current context. Used by {@link #updateAgentBodyPlan} to compute the new AgentBody's Plan.
	 */
	public GeoMovingBehavior(MovingAgentBody agentBody, Node origin, Node destination, SmartGovContext context) {
		super(agentBody, origin, destination, context, new DistanceCosts());
	}

}
