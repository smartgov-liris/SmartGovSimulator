package org.liris.smartgov.simulator.urban.geo.environment.graph;

import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.urban.geo.utils.LatLon;
import org.liris.smartgov.simulator.urban.geo.utils.lonLat.LonLat;
import org.locationtech.jts.math.Vector2D;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a geographical arc.
 * 
 * @author pbreugnot
 */
public class GeoArc extends Arc {

	@JsonIgnore
	protected Vector2D direction;
	
	/**
	 * GeoArc constructor.
	 * 
	 * @param id Arc id
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 */
	public GeoArc(String id, GeoNode startNode, GeoNode targetNode){
		super(id, startNode, targetNode, LatLon.distance(startNode.getPosition(), targetNode.getPosition()));
		this.direction = new Vector2D(new LonLat().project(startNode.getPosition()), new LonLat().project(targetNode.getPosition()));
		this.direction.normalize();
	}
	
	/**
	 * Direction of the arc.
	 *
	 * <p>
	 * Utility parameter that could be used for graphical representations,
	 * but currently not used in any process.
	 * </p>
	 * 
	 * @return arc direction
	 */
	public Vector2D getDirection() {
		return direction;
	}
}
