package smartgov.urban.geo.environment.graph;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import smartgov.core.environment.graph.Node;
import smartgov.urban.geo.utils.LatLon;

/**
 * Represents a geographical node.
 *
 * @author pbreugnot
 */
public class GeoNode extends Node {
	
	// Ignore the Coordinate field : uses the getPositionAsArray getter instead.
	@JsonIgnore
	private LatLon position;
	
	/**
	 * Minimal GeoNode constructor.
	 *
	 * @param id node id
	 * @param position node position in longitude / latitude
	 */
	public GeoNode(String id, LatLon position){
		super(id);
		this.position = position;
	}
	
	/**
	 * Creates a GeoNode with the specified incoming and outgoing arcs.
	 *
	 * @param id node id
	 * @param position node position in longitude / latitude
	 * @param outgoingArcs list of outgoing arcs
	 * @param incomingArcs list of incoming arcs
	 */
	public GeoNode(String id, LatLon position, List<? extends GeoArc> outgoingArcs, List<? extends GeoArc> incomingArcs) {
		this(id, position);
		this.outgoingArcs.addAll(outgoingArcs);
		this.incomingArcs.addAll(incomingArcs);
	}
	
	/**
	 * Geographical position of the node
	 *
	 * @return geographical node position
	 */
	@JsonProperty("position")
	public LatLon getPosition() {
		return position;
	}
}
