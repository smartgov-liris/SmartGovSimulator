package smartgov.core.environment.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;

import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;

/**
 * Generic oriented Graph.
 *
 * @see Graph
 * @author pbreugnot
 *
 * @param <Tnode> Node type
 * @param <Tarc> Arc type
 */

public class OrientedGraph<Tnode extends Node<Tarc>, Tarc extends Arc<Tnode>> extends Graph<Tnode, Tarc> {

	private MultiGraph orientedGraph;

	public OrientedGraph(Map<String, Tnode> nodes, Map<String, Tarc> arcs){
		super(nodes, new HashMap<>());
		MultiGraph g = new MultiGraph("graph");
		g.setStrict(true);
		for(Tnode node : nodes.values()){
			g.addNode(node.getId());
		}
		
		for(Tarc arc : arcs.values()) {
			g.addEdge(
						arc.getId(),
						arc.getStartNode().getId(),
						arc.getTargetNode().getId(),
						true)
			.setAttribute("distance", arc.getLength());
		}
		orientedGraph = g;
	}

	private List<String> pathBetween(Node<?> from, Node<?> to){
		 AStar astar = new AStar(orientedGraph);
		 astar.setCosts(new AStar.DefaultCosts("distance"));
		 astar.compute(from.getId(), to.getId());
		 Path path = astar.getShortestPath();
		 List<String> nodesId=new ArrayList<>();
		 if(path!=null && !path.empty()){
			 for(org.graphstream.graph.Node n: path.getNodePath())
				 nodesId.add(n.getId());
		 }
		 else //Non joinable node
			 nodesId.add(from.getId());
		 return nodesId;
	}

	private List<Tnode> pathStringToNode(List<String> nodesId){
		List<Tnode> nodesPath = new ArrayList<>();
		for(int i = 0; i < nodesId.size(); i++){
			nodesPath.add(getNodes().get(nodesId.get(i)));
		}
		return nodesPath;
	}

	public List<Tnode> shortestPath(Node<?> from, Node<?> to){
		return pathStringToNode(pathBetween(from, to));
	}

}
