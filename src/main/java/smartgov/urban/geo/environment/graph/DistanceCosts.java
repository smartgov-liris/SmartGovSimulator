package smartgov.urban.geo.environment.graph;

import java.util.Map;

import org.graphstream.algorithm.AStar.Costs;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import smartgov.urban.geo.utils.LatLon;

/**
 * AStar costs that can be used with GeoNodes and GeoArcs, that uses geographic distance
 * between nodes as the heuristic, and arc lengths as costs.
 *
 */
public class DistanceCosts implements Costs {
	
	private Map<String, ? extends smartgov.core.environment.graph.Node> nodes;
	private Map<String, ? extends smartgov.core.environment.graph.Arc> arcs;

	/**
	 * DistanceCosts constructor. Responsibility is left to the user to make sure that specified
	 * arcs and nodes (that usually come from the current SmartGovContext or Graph instance) are
	 * GeoArcs and GeoNodes, in order to avoid cumbersome map casting. If its not the case, a
	 * ClassCastException will be raised when trying to compute a cost.
	 * 
	 * @param nodes current graph geo nodes
	 * @param arcs current graph geo arcs
	 */
	public DistanceCosts(Map<String, ? extends smartgov.core.environment.graph.Node> nodes, Map<String, ? extends smartgov.core.environment.graph.Arc> arcs) {
		this.nodes = nodes;
		this.arcs = arcs;
	}

	/**
	 * Heuristic that returns the
	 * {@link smartgov.urban.geo.utils.LatLon#distance(LatLon, LatLon) geographical distance}
	 * between the specified nodes.
	 * 
	 * @param node current node
	 * @param target target node
	 * @return geographical distance between nodes
	 */
	@Override
	public double heuristic(Node node, Node target) {
		return LatLon.distance(
				((GeoNode) nodes.get(node.getId())).getPosition(),
				((GeoNode) nodes.get(target.getId())).getPosition()
				);
	}
	
	/**
	 * Returns {@link smartgov.core.environment.graph.Arc#getLength() generic arc length},
	 * that should correspond to the geographical GeoArc length in this context.
	 * 
	 * @param parent current node
	 * @param from arc linking the two nodes
	 * @param next target node
	 * @return arc length
	 */
	@Override
	public double cost(Node parent, Edge from, Node next) {
		return arcs.get(from.getId()).getLength();
	}

}
