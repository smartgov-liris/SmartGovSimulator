package org.liris.smartgov.simulator.core.environment.graph;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.environment.graph.astar.AStar;
import org.liris.smartgov.simulator.core.environment.graph.astar.Costs;

/**
 * Graph class.
 *
 * A container for arcs and nodes of
 * the simulation.
 * 
 * @author pbreugnot
 */
public class Graph {
	private Map<String, ? extends Node> nodes;
	private Map<String, ? extends Arc> arcs;
	
	/**
	 * Graph constructor.
	 *
	 * @param nodes Nodes of the graph
	 * @param arcs Arcs of the graph
	 */
	public Graph(Map<String, ? extends Node> nodes, Map<String, ? extends Arc> arcs) {
		this.nodes = nodes;
		this.arcs = arcs;
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
	
	private List<Node> pathBetween(Node from, Node to, Costs costs, MovingAgentBody agent){
		long beginTime = DateTime.now().getMillis();
		AStar astar = new AStar(this);
		astar.setCosts(costs);
		astar.compute(from.getId(), to.getId(), agent);
		List<Node> path = astar.getShortestPath();
		if(path==null || path.isEmpty()){
			throw new IllegalArgumentException("No path could be built from " + from + " to " + to);
		}
		SmartGov.logger.debug(
				"Time to compute shortest path from " + from.getId() + " to " + to.getId() + " : " + (DateTime.now().getMillis() - beginTime)
				);
		return path;
	}

//	private List<Node> pathStringToNode(List<String> nodesId){
//		List<Node> nodesPath = new ArrayList<>();
//		for(int i = 0; i < nodesId.size(); i++){
//			nodesPath.add(getNodes().get(nodesId.get(i)));
//		}
//		return nodesPath;
//	}

	/**
	 * Compute the shortest path from the specified nodes using the specified AStar costs.
	 * 
	 * @param from source node
	 * @param to target node
	 * @param costs AStar costs
	 * @return Shortest path between nodes as a node list
	 */
	public List<Node> shortestPath(Node from, Node to, Costs costs, MovingAgentBody agent){
		return pathBetween(from, to, costs, agent);
	}
	
	/**
	 * Compute the shortest path from the specified nodes using the {@link DefaultLengthCosts}.
	 * 
	 * Notice that <b>this is quite inefficient</b>, and should only be used in the case of a
	 * non-geographical graph and if no other heuristics are available.
	 * 
	 * @param from source node
	 * @param to target node
	 * @return Shortest path between nodes as a node list
	 */
	public List<Node> shortestPath(Node from, Node to, MovingAgentBody agent){
		return shortestPath(from, to, new DefaultLengthCosts(), agent);
	}

}
