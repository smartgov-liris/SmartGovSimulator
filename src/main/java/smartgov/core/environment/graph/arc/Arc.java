package smartgov.core.environment.graph.arc;

import smartgov.core.environment.graph.GraphObject;
import smartgov.core.environment.graph.node.Node;

/**
 * Generic Arc class.
 * 
 * @see Node
 * @author pbreugnot
 *
 * @param <Tnode> associated Node type.
 */
public class Arc<Tnode extends Node<?>> extends GraphObject {

	private String id;
	private final Tnode startNode;
	private final Tnode targetNode;
	protected double length;

	/**
	 * Arc constructor.
	 * 
	 * @param id Arc id
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 * @param distance Length of the Arc
	 */
	public Arc(String id, Tnode startNode, Tnode targetNode, double length) {
		this.id = id;
		this.startNode = startNode;
		this.targetNode = targetNode;
		this.length = length;
	}
	
	public String getId() {
		return id;
	}
	
	public Tnode getStartNode() {
		return startNode;
	}
	
	public Tnode getTargetNode() {
		return targetNode;
	}

	public double getLength() {
		return length;
	}

	@Override
	public String toString() {
		return new String("| " + startNode.getId() + " | " + targetNode.getId());
	}

}
