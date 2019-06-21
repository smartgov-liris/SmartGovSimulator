package smartgov.urban.geo.environment.graph;

import org.locationtech.jts.geom.Coordinate;

/**
 * A class that represents a spot on an edge.
 *
 * Can be extended to represent specific model items (e.g. : a parking spot).
 * 
 * @author pbreugnot
 *
 */
public abstract class EdgeSpot {
	
	private Coordinate projectionOnEdge;
	private Coordinate position;
	
	/**
	 * EdgeSpot constructor.
	 *
	 * @param position geographical position of the edge spot, in longitude
	 * / latitude
	 */
	// TODO: Projection on edge should be computed automatically
	public EdgeSpot(Coordinate position) {
		this.position = position;
	}

	// TODO: Maybe this should directly be a node?
	public Coordinate getProjectionOnEdge() {
		return projectionOnEdge;
	}
	
	public void setProjectionOnEdge(Coordinate projectionOnEdge) {
		this.projectionOnEdge = projectionOnEdge;
	}
	
	/**
	 * Position of the edge spot in longitude / latitude.
	 *
	 * @return edge spot coordinated
	 */
	public Coordinate getPosition() {
		return position;
	}

}
