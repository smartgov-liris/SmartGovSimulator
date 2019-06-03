package smartgov.core.environment.graph;

import java.util.Map;

import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;

/**
 * Generic Graph structure.
 * 
 * @see Arc
 * @see Node
 * @author pbreugnot
 *
 * @param <Node> Node type
 * @param <Arc> Arc type
 */
public class Graph {
	private Map<String, ? extends Node> nodes;
	private Map<String, ? extends Arc> arcs;
	
	public Graph(Map<String, ? extends Node> nodes, Map<String, ? extends Arc> arcs) {
		this.nodes = nodes;
		this.arcs = arcs;
	}
	
	public Map<String, ? extends Node> getNodes() {
		return nodes;
	}
	
	public Map<String, ? extends Arc> getArcs() {
		return arcs;
	}

}
