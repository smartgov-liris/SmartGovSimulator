package smartgov.core.agent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

/**
 * A Plan represents a path that an agent wants to follow in the environment graph.
 * 
 * @author pbreugnot
 *
 * @param <Tnode> Node type
 * @param <Tarc> Arc type
 */
public class Plan <Tnode extends Node<Tarc>, Tarc extends Arc<Tnode>> {

	private List<Tnode> nodes;
	private Queue<Tnode> remainingNodes;
	private Tnode currentNode;
	private Tarc currentArc;
	// private int indexOfCurrentNode;
	private boolean pathComplete;
	
	private AbstractAgent<?> agent;
	
	/**
	 * Empty plan for agent body pool. Need to be updated.
	 */
	public Plan() {
		this.nodes = new ArrayList<>();
	}
	
	public Plan(List<Tnode> nodes){
		update(nodes);
	}
	
	public void setAgent(AbstractAgent<?> agent) {
		this.agent = agent;
	}
	
	private Tarc findCurrentArc() {
		List<Tarc> arcs = currentNode.getOutgoingArcs();
		Tnode nextNode = remainingNodes.peek();
		if(nextNode != null){
			if(arcs.size() == 1){
				return arcs.get(0);
			} else {
				for(Tarc arc : arcs){
					if(arc.getTargetNode().getId().equals(nextNode.getId())){
						return arc;
					}
				}
			}
		}
		return null; //Should not happen !
	}
	
	public Tarc getCurrentArc() {
		return currentArc;
	}
	
	public void update(List<? extends Tnode> nodes) {
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
	
	public Tnode getNextNode(){
		return remainingNodes.peek();
	}
	
	// TODO: Is it useful?
//	public Tarc getNextArc(){
//		List<Tarc> arcs = remainingNodes.peek().getOutgoingArcs();
//		try {
//			Tnode futurNode = nodes.get(indexOfCurrentNode+2);
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
	
	public Tnode getLastNode() {
		return nodes.get(nodes.size() - 1);
	}
	
	public Tnode getPreviousNode() {
		return nodes.get(nodes.size() - 2);
	}
	
	public Tnode getCurrentNode() {
		return currentNode;
	}
	
	public List<Tnode> getNodes() {
		return nodes;
	}
	
	public void reachANode(){
		// Departure events
		if (this.currentArc != null) {
			this.currentArc.triggerAgentDepartureListeners(new AgentDeparture(agent));
			agent.getBody().triggerArcLeftListeners(new ArcLeftEvent(currentArc));
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
			agent.getBody().triggerDestinationReachedListeners(new DestinationReachedEvent(currentNode));
		}
		else {
			this.currentArc = findCurrentArc();
			// Arrival event
			this.currentArc.triggerAgentArrivalListeners(new AgentArrival(agent));
			agent.getBody().triggerArcReachedListeners(new ArcReachedEvent(this.currentArc));
		}
		
		if (this.currentNode != null) {
			// Agent arrival will also trigger SinkNode behavior. pathComplete = true must be set before.
			this.currentNode.triggerAgentArrivalListeners(new AgentArrival(agent));
			agent.getBody().triggerNodeReachedListeners(new NodeReachedEvent(currentNode));
		}
		
	}
	
	public void addANode(Tnode node){
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
