package smartgov.core.environment.graph;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.output.node.NodeIdSerializer;

/**
 * An oriented link between two nodes.
 *
 * Agents can use them to navigate between nodes.
 * 
 * <p>
 * Notice that agents does not necessarily move on arcs,
 * they can also spawn or disappear directly from nodes 
 * to others. For agents that move on arcs, see
 * {@link smartgov.core.agent.moving.MovingAgent}.
 * </p>
 *
 * @author pbreugnot
 *
 */
public class Arc extends GraphItem {

	private String id;
	@JsonSerialize(using = NodeIdSerializer.class)
	private final Node startNode;
	@JsonSerialize(using = NodeIdSerializer.class)
	private final Node targetNode;
	private double length;

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

	/**
	 * Arc id.
	 */	
	public String getId() {
		return id;
	}
	
	/**
	 * Start node.
	 */
	public Node getStartNode() {
		return startNode;
	}
	
	/**
	 * Target node.
	 */
	public Node getTargetNode() {
		return targetNode;
	}

	/**
	 * Length of the arc, arbitrary unit.
	 */
	public double getLength() {
		return length;
	}

}
