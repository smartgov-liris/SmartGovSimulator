package smartgov.core.agent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.agent.events.ArcLeftEvent;
import smartgov.core.agent.events.ArcReachedEvent;
import smartgov.core.agent.events.DestinationReachedEvent;
import smartgov.core.agent.events.NodeReachedEvent;
import smartgov.core.environment.graph.SourceNode;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.environment.graph.events.AgentDeparture;
import smartgov.core.environment.graph.events.SpawnAgentEvent;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.output.node.NodeListIdSerializer;
import smartgov.core.output.node.NodeIdSerializer;
import smartgov.core.output.arc.ArcIdSerializer;

/**
 * A Plan represents a path that an agent wants to follow in the environment graph.
 * 
 * @author pbreugnot
 *
 * @param <Node> Node type
 * @param <Arc> Arc type
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
	@JsonIgnore
	private MovingAgent agent;
	
	/**
	 * Empty plan for agent body pool. Need to be updated.
	 */
	public Plan(MovingAgent agent) {
		this.nodes = new ArrayList<>();
		this.agent = agent;
	}
	
	public Plan(List<Node> nodes){
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
	
//	public void setPathComplete(boolean pathComplete) {
//		this.pathComplete = pathComplete;
//	}
	
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
		// Departure events
		if (this.currentArc != null) {
			this.currentArc.triggerAgentDepartureListeners(new AgentDeparture(agent));
			((MovingAgentBody) agent.getBody()).triggerArcLeftListeners(new ArcLeftEvent(currentArc));
		}
		if (this.currentNode != null) {
			this.currentNode.triggerAgentDepartureListeners(new AgentDeparture(agent));
		}
		else {
			// Trigger spawn listeners.
			// Assumes that the first node of the Plan is necessarily a source node.
			((SourceNode) this.remainingNodes.peek()).triggerSpawnAgentListeners(new SpawnAgentEvent(agent));
		}
		
		this.currentNode = remainingNodes.poll();
		
		if (remainingNodes.size() == 0) {
			pathComplete = true;
			((MovingAgentBody) agent.getBody()).triggerDestinationReachedListeners(new DestinationReachedEvent(currentNode));
		}
		else {
			this.currentArc = findCurrentArc();
			// Arrival event
			this.currentArc.triggerAgentArrivalListeners(new AgentArrival(agent));
			((MovingAgentBody) agent.getBody()).triggerArcReachedListeners(new ArcReachedEvent(this.currentArc));
		}
		
		if (this.currentNode != null) {
			// Agent arrival will also trigger SinkNode behavior. pathComplete = true must be set before.
			this.currentNode.triggerAgentArrivalListeners(new AgentArrival(agent));
			((MovingAgentBody) agent.getBody()).triggerNodeReachedListeners(new NodeReachedEvent(currentNode));
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
