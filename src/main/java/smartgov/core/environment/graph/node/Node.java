package smartgov.core.environment.graph.node;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.environment.graph.GraphObject;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.output.arc.ArcListIdSerializer;

/**
 * Generic Node class.
 * 
 * @see Arc
 * @author pbreugnot
 *
 * @param <T> Associated Arc type
 */
public class Node extends GraphObject {
	
	public String id;
	@JsonSerialize(using = ArcListIdSerializer.class)
	protected List<Arc> outgoingArcs;
	@JsonSerialize(using = ArcListIdSerializer.class)
	protected List<Arc> incomingArcs;
	
	public Node(String id) {
		this(id, new ArrayList<>(), new ArrayList<>());
	}
	
	public Node(String id, List<Arc> outgoingArcs, List<Arc> incomingArcs) {
		this.id = id;
		this.outgoingArcs = outgoingArcs;
		this.incomingArcs = incomingArcs;
	}
	
	public String getId() {
		return id;
	}
	
	public void setIncomingArcs(List<Arc> incomingArcs) {
		this.incomingArcs = incomingArcs;
	}
	
	public void setOutgoingArcs(List<Arc> outgoingArcs) {
		this.outgoingArcs = outgoingArcs;
	}
	
	public void addIncomingArc(Arc incomingArc){
		if(!incomingArcs.contains(incomingArc)){
			incomingArcs.add(incomingArc);
		}
	}
	
	public void addOutgoingArc(Arc outgoingArc){
		if(!outgoingArcs.contains(outgoingArc)){
			outgoingArcs.add(outgoingArc);
		}
	}
	
	public List<Arc> getIncomingArcs() {
		return incomingArcs;
	}
	
	public List<Arc> getOutgoingArcs() {
		return outgoingArcs;
	}
}
