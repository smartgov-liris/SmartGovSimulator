package org.liris.smartgov.simulator.core.environment.graph;

import org.liris.smartgov.simulator.core.environment.graph.astar.Costs;

/**
 * A default AStar cost implementation, that uses the generic arc length as a cost.
 * 
 * <p>
 * Notice that the heuristic <b>always returns 0</b>, what makes AStar much slower.
 * </p>
 * 
 * However, this class exists to provide default costs for arc or nodes that are not
 * geo-localized. {@link org.liris.smartgov.simulator.urban.geo.environment.graph.DistanceCosts} or an
 * other <a href="https://data.graphstream-project.org/api/gs-algo/current/">AStart costs</a>
 * implementation should be used whenever its possible.
 * 
 */
public class DefaultLengthCosts implements Costs {

	/**
	 * DefaultLengthCost constructor.
	 * 
	 */
	public DefaultLengthCosts() {
	}
	
	/**
	 * Null heuristic.
	 * 
	 * @param node current node
	 * @param target target node
	 * @return always 0
	 */
	@Override
	public double heuristic(Node node, Node target) {
		return 0;
	}

	/**
	 * Default cost, that returns {@link org.liris.smartgov.simulator.core.environment.graph.Arc#getLength() generic arc length}.
	 * 
	 * @param edge current arc
	 * @return arc length
	 */
	@Override
	public double cost(Arc edge) {
		return edge.getLength();
	}

}
