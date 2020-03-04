package org.liris.smartgov.simulator.urban.geo.environment.graph;


import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.environment.graph.astar.Costs;
import org.liris.smartgov.simulator.urban.geo.utils.LatLon;

/**
 * AStar costs that can be used with GeoNodes and GeoArcs, that uses geographic distance
 * between nodes as the heuristic, and arc lengths as costs.
 *
 */
public class DistanceCosts implements Costs {

	/**
	 * DistanceCosts constructor.
	 */
	public DistanceCosts() {

	}

	/**
	 * Heuristic that returns the
	 * {@link org.liris.smartgov.simulator.urban.geo.utils.LatLon#distance(LatLon, LatLon) geographical distance}
	 * between the specified nodes.
	 * 
	 * @param node current node
	 * @param target target node
	 * @return geographical distance between nodes
	 */
	@Override
	public double heuristic(Node node, Node target) {
		return LatLon.distance(
				((GeoNode) node).getPosition(),
				((GeoNode) target).getPosition()
				);
	}
	
	/**
	 * Returns {@link org.liris.smartgov.simulator.core.environment.graph.Arc#getLength() generic arc length},
	 * that should correspond to the geographical GeoArc length in this context.
	 * 
	 * @param edge current geographical arc
	 * @return arc length
	 */
	@Override
	public double cost(Arc edge) {
		return edge.getLength();
	}

	@Override
	public double cost(Arc arc, MovingAgentBody body) {
		return cost(arc);
	}

}
