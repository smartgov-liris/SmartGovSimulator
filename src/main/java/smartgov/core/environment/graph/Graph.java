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
 * @param <Tnode> Node type
 * @param <Tarc> Arc type
 */
public class Graph<Tnode extends Node<?>, Tarc extends Arc<?>> {
	private Map<String, ? extends Tnode> nodes;
	private Map<String, ? extends Tarc> arcs;
	
	public Graph(Map<String, ? extends Tnode> nodes, Map<String, ? extends Tarc> arcs) {
		this.nodes = nodes;
		this.arcs = arcs;
	}
	
	public Map<String, ? extends Tnode> getNodes() {
		return nodes;
	}
	
	public Map<String, ? extends Tarc> getArcs() {
		return arcs;
	}

}
