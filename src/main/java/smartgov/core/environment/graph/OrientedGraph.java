package smartgov.core.environment.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.IdAlreadyInUseException;
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
	
	public OrientedGraph(Map<String, Tnode> nodes){
		super(nodes, new HashMap<>());
		MultiGraph g = new MultiGraph("graph");
		g.setStrict(true);
        for(Tnode node : nodes.values()){
        	g.addNode(node.getId());
        }
        
       for(Tnode node : nodes.values()){
        	for(int j = 0; j < node.getOutgoingArcs().size(); j++){       	
        		try {
       				g.addEdge(node.getOutgoingArcs().get(j).getId(), 
    				node.getId(),
    				node.getOutgoingArcs().get(j).getTargetNode().getId(), true)
    				.setAttribute("distance", node.getOutgoingArcs().get(j).getDistance());
       			} catch(IdAlreadyInUseException | ElementNotFoundException |EdgeRejectedException er ){
       				System.out.println(er.getMessage());
       			};
        	}	
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




/*
package graph;

import java.util.ArrayList;
import java.util.List;

import environment.city.EnvVar;

import environment.city.EnvVar;
import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;

public class OrientedGraph {
	
	private HipsterDirectedGraph<String, Double> orientedGraph;
	
	private List<Node> nodes;
	
	public OrientedGraph(){
		this.nodes = EnvVar.nodes;
		GraphBuilder<String, Double> graph = GraphBuilder.<String, Double>create();
        for(int i = 0; i < EnvVar.nodes.size(); i++){
        	Node node = EnvVar.nodes.get(i);
        	
        	for(int j = 0; j < node.getOutgoingArcs().size(); j++){
        		graph.connect(node.getId())
        		.to(node.getOutgoingArcs().get(j).getTargetNode().getId())
        		.withEdge(node.getOutgoingArcs().get(j).getDistance());
        	}
        }
        orientedGraph = graph.createDirectedGraph();
	}
	
	
	public OrientedGraph(List<Node> nodes){
		this.nodes = nodes;
		GraphBuilder<String, Double> graph = GraphBuilder.<String, Double>create();
        for(int i = 0; i < nodes.size(); i++){
        	Node node = nodes.get(i);
        	
        	for(int j = 0; j < node.getOutgoingArcs().size(); j++){
        		graph.connect(node.getId())
        		.to(node.getOutgoingArcs().get(j).getTargetNode().getId())
        		.withEdge(node.getOutgoingArcs().get(j).getDistance());
        	}
        }
        orientedGraph = graph.createDirectedGraph();
	}
	
	
	private List<String> pathBetween(Node from, Node to){
		SearchProblem<Double, String, WeightedNode<Double, String, Double>> p = GraphSearchProblem.startingFrom(from.getId())
                .in(orientedGraph)
                .takeCostsFromEdges()
                .build();
		return AStar.recoverStatePath(Hipster.createAStar(p).search(to.getId()).getGoalNode());
	}
	
	private List<Node> pathStringToNode(List<String> nodesId){
		List<Node> nodesPath = new ArrayList<>();
		for(int i = 0; i < nodesId.size(); i++){
			nodesPath.add(nodes.get(Integer.parseInt(nodesId.get(i))));
		}
		return nodesPath;
	}
	
	public List<Node> shortestPath(Node from, Node to){
		return pathStringToNode(pathBetween(from, to));
	}
	
}

*/