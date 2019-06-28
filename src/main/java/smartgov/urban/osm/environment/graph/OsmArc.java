package smartgov.urban.osm.environment.graph;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import smartgov.urban.geo.environment.graph.GeoArc;

/**
 * Class used to represents OSM arcs, i.e. roads.
 * 
 * @see GeoArc
 * @see OsmNode
 * 
 * @author pbreugnot
 *
 */
@JsonIgnoreProperties({"road", "lanes", "spots", "closeSpot", "spotCloseToNode", "aspotAvailable"})
public class OsmArc extends GeoArc {
	
	public enum RoadDirection {FORWARD, BACKWARD}
	
	protected Road road;
	protected RoadDirection roadDirection;
	
	/**
	 * OsmArc constructor.
	 * 
	 * @param id Arc id
	 * @param road Road to which the Arc belongs to.
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 * @param type Arc type. (OSM 'highway' attribute)
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
	}
	
	public Road getRoad() {
		return road;
	}
	
	public RoadDirection getRoadDirection() {
		return roadDirection;
	}
	
}
