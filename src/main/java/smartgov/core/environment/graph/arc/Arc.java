package smartgov.core.environment.graph.arc;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.environment.graph.GraphItem;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.output.node.NodeIdSerializer;

/**
 * Generic Arc class.
 * 
 * @see Node
 * @author pbreugnot
 *
 * @param <Tnode> associated Node type.
 */
public class Arc extends GraphItem {

	private String id;
	@JsonSerialize(using = NodeIdSerializer.class)
	private final Node startNode;
	@JsonSerialize(using = NodeIdSerializer.class)
	private final Node targetNode;
	protected double length;

	/**
	 * Arc constructor.
	 * 
	 * @param id Arc id
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 * @param distance Length of the Arc
	 */
	public Arc(String id, Node startNode, Node targetNode, double length) {
		this.id = id;
		this.startNode = startNode;
		startNode.addOutgoingArc(this);
		this.targetNode = targetNode;
		targetNode.addIncomingArc(this);
		this.length = length;
	}
	
	public String getId() {
		return id;
	}
	
	public Node getStartNode() {
		return startNode;
	}
	
	public Node getTargetNode() {
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
