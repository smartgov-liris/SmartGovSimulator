package smartgov.urban.geo.environment.graph;

import org.locationtech.jts.math.Vector2D;

import com.fasterxml.jackson.annotation.JsonIgnore;

import smartgov.core.environment.graph.arc.Arc;
import smartgov.urban.geo.simulation.GISComputation;

/**
 * Generic class to represent an Arc on a map.
 * 
 * @see Arc
 * @author pbreugnot
 *
 * @param <Tnode> GeoNode type
 */
public class GeoArc<Tnode extends GeoNode<?>> extends Arc<Tnode> {

	@JsonIgnore
	protected Vector2D direction;
	
	/**
	 * GeoArc constructor.
	 * 
	 * @param geography Current Geometry
	 * @param id Arc id
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 * @param polyLine Shape of the Arc
	 */
	public GeoArc(String id, Tnode startNode, Tnode targetNode){
		super(id, startNode, targetNode, GISComputation.GPS2Meter(startNode.getPosition(), targetNode.getPosition()));
		this.direction = new Vector2D(startNode.getPosition(), targetNode.getPosition());
		this.direction.normalize();
	}
	
	public Vector2D getDirection() {
		return direction;
	}
}
