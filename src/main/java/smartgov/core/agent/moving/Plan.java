package smartgov.core.agent.moving;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.output.node.NodeListIdSerializer;
import smartgov.core.output.node.NodeIdSerializer;
import smartgov.core.output.arc.ArcIdSerializer;

/**
 * A Plan represents a path that an agent wants to follow in the environment graph.
 * 
 * @author pbreugnot
 */
public class Plan {

	@JsonSerialize(using=NodeListIdSerializer.class)
	private List<Node> nodes;
	@JsonIgnore
	private Queue<Node> remainingNodes;
	@JsonSerialize(using=NodeIdSerializer.class)
	private Node currentNode;
	@JsonSerialize(using=ArcIdSerializer.class)
	private Arc currentArc;
	@JsonIgnore
	private boolean pathComplete;
	/**
	 * Plan constructor.
	 */
	public Plan() {
		this.nodes = new ArrayList<>();
	}
	
	/**
	 * Empty plan for agent body pool. Need to be updated.
	 */
	public Plan(List<? extends Node> nodes) {
		this();
		update(nodes);
	}
	
	private Arc findCurrentArc() {
		List<Arc> arcs = currentNode.getOutgoingArcs();
		Node nextNode = remainingNodes.peek();
		if(nextNode != null){
			if(arcs.size() == 1){
				return arcs.get(0);
			} else {
				for(Arc arc : arcs){
					if(arc.getTargetNode().getId().equals(nextNode.getId())){
						return arc;
					}
				}
			}
		}
		return null; //Should not happen !
	}
	
	public Arc getCurrentArc() {
		return currentArc;
	}
	
	public void update(List<? extends Node> nodes) {
		this.nodes.clear();
		this.nodes.addAll(nodes);
		this.remainingNodes = new LinkedList<>();
		remainingNodes.addAll(nodes);
		this.pathComplete = false;
		this.currentNode = null;
		this.currentArc = null;
		reachANode();
	}
	
	public boolean isPathComplete() {
		return pathComplete;
	}
	
	@JsonIgnore
	public Node getNextNode(){
		return remainingNodes.peek();
	}
	
	// TODO: Is it useful?
//	public Arc getNextArc(){
//		List<Arc> arcs = remainingNodes.peek().getOutgoingArcs();
//		try {
//			Node futurNode = nodes.get(indexOfCurrentNode+2);
//			if(futurNode != null){
//				if(arcs.size() == 1){
//					return arcs.get(0);
//				} else {
//					for(int i = 0; i < arcs.size(); i++){
//						if(arcs.get(i).getTargetNode().getId().equals(futurNode.getId())){
//							return arcs.get(i);
//						}
//					}
//				}
//			}
//		} catch (IndexOutOfBoundsException e){
//			
//		}
//		return null;
//	}
	@JsonIgnore
	public Node getLastNode() {
		return nodes.get(nodes.size() - 1);
	}
	
	@JsonIgnore
	public Node getPreviousNode() {
		return nodes.get(nodes.size() - 2);
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public void reachANode(){
		this.currentNode = remainingNodes.poll();
		
		if (remainingNodes.size() == 0) {
			pathComplete = true;
		}
		else {
			this.currentArc = findCurrentArc();
		}
		
	}
	
	public void addANode(Node node){
		this.nodes.add(node);
		this.remainingNodes.add(node);
		this.pathComplete = false;
	}
	
	@Override
	public String toString() {
		String s = new String();
		for(int indexOfNode = 0; indexOfNode < nodes.size(); indexOfNode++){
			s+= indexOfNode + ") " + nodes.get(indexOfNode).getId() + ", ";
		}
		return s;
	}
	
}
