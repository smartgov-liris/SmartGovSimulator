package smartgov.urban.osm.environment.graph;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.osm.output.RoadIdSerializer;

/**
 * Class used to represent arcs between OsmNodes.
 *
 * <p>
 * Can be seen as a part of an OSM way.
 * </p>
 * 
 * @author pbreugnot
 *
 */
@JsonIgnoreProperties({"road"})
public class OsmArc extends GeoArc {
	
	/**
	 * Used to represent the direction represented by an arc in the osm
	 * road.
	 */
	public enum RoadDirection {
		/**
		 * When the arc links nodes in the order of way nodes.
		 */
		FORWARD,
		/**
		 * When the arc links nodes in the opposite order of way nodes.
		 */
		BACKWARD}
	
	/**
	 * Road to which this arc belongs to.
	 */
	@JsonSerialize(using = RoadIdSerializer.class)
	protected Road road;
	/**
	 * Direction represented by the arc in the associated road.
	 */
	protected RoadDirection roadDirection;
	
	/**
	 * OsmArc constructor.
	 * 
	 * @param id Arc id
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 * @param road Road to which the Arc belongs to.
	 * @param roadDirection corresponding direction of the road
	 */
	public OsmArc(
			String id,
			OsmNode startNode,
			OsmNode targetNode,
			Road road,
			RoadDirection roadDirection) {
		super(id, startNode, targetNode);
		this.road = road;
		this.roadDirection = roadDirection;
		this.road.setOutgoingArcForNode(startNode.getId(), this);
	}
	
	/**
	 * Road that this arc represents, as a part of it.
	 *
	 * @return osm road
	 */
	public Road getRoad() {
		return road;
	}
	
	/**
	 * Direction that the arc represents in the associated road.
	 *
	 * @return road direction
	 */
	public RoadDirection getRoadDirection() {
		return roadDirection;
	}
	
}
