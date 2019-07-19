package smartgov.urban.geo.environment.graph;

import smartgov.urban.geo.utils.LatLon;

/**
 * A class that represents a spot on an edge.
 *
 * Can be extended to represent specific model items (e.g. : a parking spot).
 * 
 * @author pbreugnot
 *
 */
public abstract class EdgeSpot {
	
	private LatLon projectionOnEdge;
	private LatLon position;
	
	/**
	 * EdgeSpot constructor.
	 *
	 * @param position geographical position of the edge spot
	 */
	// TODO: Projection on edge should be computed automatically
	public EdgeSpot(LatLon position) {
		this.position = position;
	}

	// TODO: Maybe this should directly be a node?
	public LatLon getProjectionOnEdge() {
		return projectionOnEdge;
	}
	
	public void setProjectionOnEdge(LatLon projectionOnEdge) {
		this.projectionOnEdge = projectionOnEdge;
	}
	
	/**
	 * Position of the edge spot in longitude / latitude.
	 *
	 * @return edge spot coordinated
	 */
	public LatLon getPosition() {
		return position;
	}

}
