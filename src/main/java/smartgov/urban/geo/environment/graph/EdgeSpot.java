package smartgov.urban.geo.environment.graph;

import org.locationtech.jts.geom.Coordinate;

/**
 * A class that represents a spot on an edge. Can be extended to represent specific model items (e.g. : a parking spot).
 * 
 * @author pbreugnot
 *
 */
public abstract class EdgeSpot {
	
	private Coordinate projectionOnEdge;
	private Coordinate position;
	
	public EdgeSpot(Coordinate position) {
		this.position = position;
	}
	public Coordinate getProjectionOnEdge() {
		return projectionOnEdge;
	}
	
	public void setProjectionOnEdge(Coordinate projectionOnEdge) {
		this.projectionOnEdge = projectionOnEdge;
	}
	
	public Coordinate getPosition() {
		return position;
	}

}
