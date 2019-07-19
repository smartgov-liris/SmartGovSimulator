package smartgov.core.environment.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.joda.time.DateTime;

import smartgov.SmartGov;

/**
 * Graph class.
 *
 * Basically a container for arcs and nodes of
 * the simulation.
 * 
 * @author pbreugnot
 */
public class Graph {
	private Map<String, ? extends Node> nodes;
	private Map<String, ? extends Arc> arcs;
	
	private MultiGraph orientedGraph;
	
	/**
	 * Graph constructor.
	 *
	 * @param nodes Nodes of the graph
	 * @param arcs Arcs of the graph
	 */
	public Graph(Map<String, ? extends Node> nodes, Map<String, ? extends Arc> arcs) {
		this.nodes = nodes;
		this.arcs = arcs;
		
		MultiGraph g = new MultiGraph("graph", true, false, nodes.size(), arcs.size());
		// g.setStrict(true);
		for(Node node : nodes.values()){
			g.addNode(node.getId()).setAttribute("id", node.getId());;
		}

		for(Arc arc : arcs.values()) {
			Edge edge = g.addEdge(
						arc.getId(),
						arc.getStartNode().getId(),
						arc.getTargetNode().getId(),
						true);
			edge.setAttribute("length", arc.getLength());
			edge.setAttribute("id", arc.getId());
		}
		orientedGraph = g;
	}
	
	/**
	 * Nodes of the graph.
	 *
	 * @return <code>id: node</code> map of the nodes in this graph
	 */
	public Map<String, ? extends Node> getNodes() {
		return nodes;
	}
	
	/**
	 * Arcs of the graph.
	 *
	 * @return <code>id: arc</code> map of the arcs in this graph
	 */
	public Map<String, ? extends Arc> getArcs() {
		return arcs;
	}
	
	private List<String> pathBetween(Node from, Node to, AStar.Costs costs){
		long beginTime = DateTime.now().getMillis();
		AStar astar = new AStar(orientedGraph);
		astar.setCosts(costs);
		astar.compute(from.getId(), to.getId());
		Path path = astar.getShortestPath();
		List<String> nodesId=new ArrayList<>();
		if(path!=null && !path.empty()){
			for(org.graphstream.graph.Node n: path.getNodePath())
				nodesId.add(n.getId());
			}
		else {
			throw new IllegalArgumentException("No path could be built from " + from + " to " + to);
		}
		SmartGov.logger.debug(
				"Time to compute shortest path from " + from.getId() + " to " + to.getId() + " : " + (DateTime.now().getMillis() - beginTime)
				);
		return nodesId;
	}

	private List<Node> pathStringToNode(List<String> nodesId){
		List<Node> nodesPath = new ArrayList<>();
		for(int i = 0; i < nodesId.size(); i++){
			nodesPath.add(getNodes().get(nodesId.get(i)));
		}
		return nodesPath;
	}

	public List<Node> shortestPath(Node from, Node to, AStar.Costs costs){
		return pathStringToNode(pathBetween(from, to, costs));
	}
	
	public List<Node> shortestPath(Node from, Node to){
		return shortestPath(from, to, new AStar.DefaultCosts("length"));
	}

}
