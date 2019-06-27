package smartgov.urban.osm.environment.graph;

import org.locationtech.jts.geom.Coordinate;

import smartgov.urban.geo.environment.graph.GeoNode;

/**
 * Class used to represents OSM nodes, i.e. crossroads and line breaks.
 * 
 * 
 * @author pbreugnot
 *
 */
public class OsmNode extends GeoNode {

	// TODO : serialize as id?
	private Road road;
	
	public OsmNode(String id, Coordinate coordinate, Road road) {
		super(id, coordinate);
		this.road = road;
	}

	public Road getRoad() {
		return road;
	}

}
