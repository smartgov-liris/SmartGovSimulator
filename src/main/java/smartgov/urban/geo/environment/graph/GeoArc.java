package smartgov.urban.geo.environment.graph;

import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.math.Vector2D;

import smartgov.core.environment.graph.arc.Arc;
import smartgov.urban.geo.simulation.GISComputation;

/**
 * Generic abstract class to represent an Arc on a map.
 * 
 * @see Arc
 * @author pbreugnot
 *
 * @param <Tnode> GeoNode type
 */
public abstract class GeoArc<Tnode extends GeoNode<?>> extends Arc<Tnode> {

	protected Vector2D direction;
	protected MultiLineString polyline;
	
	/**
	 * GeoArc constructor.
	 * 
	 * @param geography Current Geometry
	 * @param id Arc id
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 * @param polyLine Shape of the Arc
	 */
	public GeoArc(String id, Tnode startNode, Tnode targetNode, MultiLineString polyline){
		super(id, startNode, targetNode, GISComputation.GPS2Meter(startNode.getPosition(), targetNode.getPosition()));
		this.polyline = polyline;
		this.direction = new Vector2D(startNode.getPosition(), targetNode.getPosition());
		this.direction.normalize();
	}
	
	public Vector2D getDirection() {
		return direction;
	}
}
