package smartgov.urban.geo.environment.graph;

import java.util.Map;

import org.graphstream.algorithm.AStar.Costs;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import smartgov.urban.geo.utils.LatLon;

public class DistanceCosts implements Costs {
	
	private Map<String, smartgov.core.environment.graph.Node> nodes;

	public DistanceCosts(Map<String, smartgov.core.environment.graph.Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public double heuristic(Node node, Node target) {
		return LatLon.distance(
				((GeoNode) nodes.get(node.getAttribute("id"))).getPosition(),
				((GeoNode) nodes.get(target.getAttribute("id"))).getPosition()
				);
	}

	@Override
	public double cost(Node parent, Edge from, Node next) {
		return from.getAttribute("length");
	}

}
