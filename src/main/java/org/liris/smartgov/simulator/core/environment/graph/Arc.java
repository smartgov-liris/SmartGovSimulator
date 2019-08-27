package org.liris.smartgov.simulator.core.environment.graph;

import org.liris.smartgov.simulator.core.output.node.NodeIdSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * An oriented link between two nodes.
 *
 * Agents can use them to navigate between nodes.
 * 
 * <p>
 * Notice that agents does not necessarily move on arcs,
 * they can also spawn or disappear directly from nodes 
 * to others. For agents that move on arcs, see
 * {@link org.liris.smartgov.simulator.core.agent.moving.MovingAgent}.
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
	 * @param length Length of the Arc
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
	 *
	 * @return arc id
	 */	
	public String getId() {
		return id;
	}
	
	/**
	 * Start node.
	 *
	 * @return start node
	 */
	public Node getStartNode() {
		return startNode;
	}
	
	/**
	 * Target node.
	 *
	 * @return target node
	 */
	public Node getTargetNode() {
		return targetNode;
	}

	/**
	 * Length of the arc, arbitrary unit.
	 *
	 * @return arc length
	 */
	public double getLength() {
		return length;
	}

}
