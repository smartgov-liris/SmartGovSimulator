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
	 * Construct a Plan and update it with the provided nodes.
	 *
	 * @param nodes initial nodes
	 */
	public Plan(List<? extends Node> nodes) {
		this();
		update(nodes);
	}
	
	/*
	 * Find the arc that link the current node to the next node.
	 */
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
	
	/*
	 * Current arc.
	 */
	public Arc getCurrentArc() {
		return currentArc;
	}
	
	/**
	 * Re-initialize the plan with the provided nodes.
	 *
	 * <p>
	 * Clear the previous state, even if the plan was not complete, add all
	 * the provided nodes to the plan, and reach the first node of the plan.
	 * </p>
	 */
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
	
	/*
	 * True when the last node of the plan has been reached.
	 */
	public boolean isPathComplete() {
		return pathComplete;
	}
	
	/**
	 * Next node of the plan.
	 *
	 * Correspond to the node right after the current node.
	 */
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

	/**
	 * Last node of the plan.
	 *
	 * Should correspond to the destination of the corresponding agent.
	 */
	@JsonIgnore
	public Node getLastNode() {
		return nodes.get(nodes.size() - 1);
	}
	
	/**
	 * Current node of the plan.
	 */
	public Node getCurrentNode() {
		return currentNode;
	}
	
	/**
	 * Nodes of the plan, ordered from origin to destination.
	 */
	public List<Node> getNodes() {
		return nodes;
	}
	
	/**
	 * Reach the next node of the plan, if it exists.
	 * 
	 * @throws IllegalStateException if the plan is already complete
	 */
	public void reachANode(){
		if (remainingNodes.size() == 0) {
			throw new IllegalStateException("The Plan is already complete.");
		}

		this.currentNode = remainingNodes.poll();
		
		if (remainingNodes.size() == 0) {
			pathComplete = true;
		}
		else {
			this.currentArc = findCurrentArc();
		}
		
	}
	
	/*
	 * Interesting feature, but not used for now.
	 * We should think about how this could interact
	 * with the MovingBehavior.
	 */
//	public void addANode(Node node){
//		this.nodes.add(node);
//		this.remainingNodes.add(node);
//		this.pathComplete = false;
//	}
	
}
