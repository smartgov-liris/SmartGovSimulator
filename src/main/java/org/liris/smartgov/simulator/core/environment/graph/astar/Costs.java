package org.liris.smartgov.simulator.core.environment.graph.astar;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;

/**
 * Costs used by the {@link AStar} algorithm to compute shortest
 * paths.
 *
 */
public interface Costs {
	
	/**
	 * Estimation of the cost from current node to target.
	 * 
	 * @param current current node
	 * @param target objective node
	 * @return estimated cost
	 */
	public double heuristic(Node current, Node target);
	
	/**
	 * Cost associated with the given arc.
	 * 
	 * @param arc current arc
	 * @return associated cost
	 */
	public double cost(Arc arc);
	
	
	/**
	 * Cost associated with the given arc depending of the body of the agent
	 * @param arc current arc
	 * @param body the body of the agent
	 * @return associated cost
	 */
	public double cost(Arc arc, MovingAgentBody body);
}
