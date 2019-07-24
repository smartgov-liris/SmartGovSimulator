package smartgov.core.environment.graph;

import smartgov.core.environment.graph.astar.Costs;

/**
 * A default AStar cost implementation, that uses the generic arc length as a cost.
 * 
 * <p>
 * Notice that the heuristic <b>always returns 0</b>, what makes AStar much slower.
 * </p>
 * 
 * However, this class exists to provide default costs for arc or nodes that are not
 * geo-localized. {@link smartgov.urban.geo.environment.graph.DistanceCosts} or an
 * other <a href="https://data.graphstream-project.org/api/gs-algo/current/">AStart costs</a>
 * implementation should be used whenever its possible.
 * 
 */
public class DefaultLengthCosts implements Costs {

	/**
	 * DefaultLengthCost constructor.
	 * 
	 * @param arcs arcs map containing arcs of the current graph
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
	 * Default cost, that returns {@link smartgov.core.environment.graph.Arc#getLength() generic arc length}.
	 * 
	 * @param parent current node
	 * @param from arc linking the two nodes
	 * @param next target node
	 * @return arc length
	 */
	@Override
	public double cost(Arc edge) {
		return edge.getLength();
	}

}
