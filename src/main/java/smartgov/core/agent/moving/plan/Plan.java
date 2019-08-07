package smartgov.core.agent.moving.plan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.events.EventHandler;
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
	private boolean planComplete;
	
	@JsonIgnore
	private List<EventHandler<NextNodeEvent>> nextNodeEventHandlers;
	@JsonIgnore
	private List<EventHandler<FirstNodeEvent>> firstNodeEventHandlers;
	
	/**
	 * Plan constructor.
	 */
	public Plan() {
		this.nodes = new ArrayList<>();
		this.nextNodeEventHandlers = new ArrayList<>();
		this.firstNodeEventHandlers = new ArrayList<>();
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
			throw new IllegalStateException("No arc found from node " + currentNode.getId() + " to " + nextNode.getId());
		}
		throw new IllegalStateException("No next node available, the plan may be complete.");
	}
	
	/**
	 * Current arc.
	 *
	 * @return current arc of this plan
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
	 *
	 * @param nodes new nodes of the plan
	 */
	public void update(List<? extends Node> nodes) {
		if(nodes.size() < 2) {
			throw new IllegalArgumentException("The plan should be updated with at least two nodes.");
		}
		this.nodes.clear();
		this.nodes.addAll(nodes);
		this.remainingNodes = new LinkedList<>();
		remainingNodes.addAll(nodes);
		this.planComplete = false;
//		this.currentNode = null;
//		this.currentArc = null;
		reachFirstNode();
	}
	
	/*
	 * True when the last node of the plan has been reached.
	 *
	 * @return true if and only if the plan is complete
	 */
	public boolean isComplete() {
		return planComplete;
	}
	
	/**
	 * Next node of this plan.
	 *
	 * Correspond to the node right after the current node.
	 *
	 * @return the next node of this plan
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
	 * Last node of this plan.
	 *
	 * Should correspond to the destination of the corresponding agent.
	 *
	 * @return last node of this plan
	 */
	@JsonIgnore
	public Node getLastNode() {
		return nodes.get(nodes.size() - 1);
	}
	
	/**
	 * Current node of this plan.
	 *
	 * @return current node of this plan
	 */
	public Node getCurrentNode() {
		return currentNode;
	}
	
	/**
	 * Nodes of this plan, ordered from origin to destination.
	 *
	 * @return list of nodes of this plan
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * Reach the next node of the plan, if it exists.
	 * 
	 * @throws IllegalStateException if the plan is already complete
	 */
	public void reachNextNode(){
		if (remainingNodes.size() == 0) {
			throw new IllegalStateException("The Plan is already complete.");
		}
		Node oldNode = this.currentNode;
		Arc oldArc = this.currentArc;

		this.currentNode = remainingNodes.poll();
		
		if (remainingNodes.size() == 0) {
			planComplete = true;
		}
		else {
			this.currentArc = findCurrentArc();
		}
		
		triggerNextNodeListeners(
				new NextNodeEvent(
						oldArc,
						this.currentArc,
						oldNode,
						this.currentNode
						)
				);
		
	}
	
	private void reachFirstNode() {
		this.currentNode = remainingNodes.poll();
		this.currentArc = findCurrentArc();
		
		triggerFirstNodeListeners(
				new FirstNodeEvent(
						this.currentNode,
						this.currentArc
						)
				);
	}
	
	public void addNextNodeListener(EventHandler<NextNodeEvent> listener) {
		this.nextNodeEventHandlers.add(listener);
	}
	
	private void triggerNextNodeListeners(NextNodeEvent event) {
		for (EventHandler<NextNodeEvent> listener : nextNodeEventHandlers) {
			listener.handle(event);
		}
	}
	
	public void addFirstNodeListener(EventHandler<FirstNodeEvent> listener) {
		this.firstNodeEventHandlers.add(listener);
	}
	
	private void triggerFirstNodeListeners(FirstNodeEvent event) {
		for (EventHandler<FirstNodeEvent> listener : firstNodeEventHandlers) {
			listener.handle(event);
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
