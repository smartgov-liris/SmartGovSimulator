package smartgov.core.environment.graph.node;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.environment.graph.GraphObject;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.output.node.ArcIdSerializer;

/**
 * Generic Node class.
 * 
 * @see Arc
 * @author pbreugnot
 *
 * @param <T> Associated Arc type
 */
public class Node<T extends Arc<?>> extends GraphObject {
	
	public String id;
	@JsonSerialize(using = ArcIdSerializer.class)
	protected List<T> outgoingArcs;
	@JsonSerialize(using = ArcIdSerializer.class)
	protected List<T> incomingArcs;
	
	public Node(String id) {
		this(id, new ArrayList<>(), new ArrayList<>());
	}
	
	public Node(String id, List<T> outgoingArcs, List<T> incomingArcs) {
		this.id = id;
		this.outgoingArcs = outgoingArcs;
		this.incomingArcs = incomingArcs;
	}
	
	public String getId() {
		return id;
	}
	
	public void setIncomingArcs(List<T> incomingArcs) {
		this.incomingArcs = incomingArcs;
	}
	
	public void setOutgoingArcs(List<T> outgoingArcs) {
		this.outgoingArcs = outgoingArcs;
	}
	
	public void addAnIncomingArc(T incomingArc){
		/*
		if(!outgoingArcs.contains(incomingArc)){
			outgoingArcs.add(incomingArc);
		}
		*/
		if(!incomingArcs.contains(incomingArc)){
			incomingArcs.add(incomingArc);
		}
	}
	
	public void addAnOutgoingArc(T outgoingArc){
		/*
		if(!incomingArcs.contains(outgoingArc)){
			incomingArcs.add(outgoingArc);
		}
		*/
		if(!outgoingArcs.contains(outgoingArc)){
			outgoingArcs.add(outgoingArc);
		}
	}
	
	public List<T> getIncomingArcs() {
		return incomingArcs;
	}
	
	public List<T> getOutgoingArcs() {
		return outgoingArcs;
	}
}
