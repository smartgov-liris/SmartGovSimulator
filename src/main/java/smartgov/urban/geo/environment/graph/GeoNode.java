package smartgov.urban.geo.environment.graph;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import smartgov.core.environment.graph.Node;

/**
 * Represents a geographical node.
 *
 * @author pbreugnot
 */
public class GeoNode extends Node {
	
	// Ignore the Coordinate field : uses the getPositionAsArray getter instead.
	@JsonIgnore
	private Coordinate position;
	
	/**
	 * Minimal GeoNode constructor.
	 *
	 * @param id node id
	 * @param position node position in longitude / latitude
	 */
	public GeoNode(String id, Coordinate position){
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
	public GeoNode(String id, Coordinate position, List<? extends GeoArc> outgoingArcs, List<? extends GeoArc> incomingArcs) {
		this(id, position);
		this.outgoingArcs.addAll(outgoingArcs);
		this.incomingArcs.addAll(incomingArcs);
	}
	
	/**
	 * Position of this node in longitude / latitude.
	 *
	 * @return longitude / latitude coordinates
	 */
	public Coordinate getPosition() {
		return position;
	}
	
	@JsonProperty("position")
	/**
	 * Current position as a longitude / latitude array.
	 *
	 * @return longitude / latitude array
	 */
	public double[] getPositionAsArray(){
		double[] coords = new double[2];
		coords[0] = position.x;
		coords[1] = position.y;
		return coords;
	}
}
