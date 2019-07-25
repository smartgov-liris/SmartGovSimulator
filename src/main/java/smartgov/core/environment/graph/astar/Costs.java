package smartgov.core.environment.graph.astar;

import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;

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
}
