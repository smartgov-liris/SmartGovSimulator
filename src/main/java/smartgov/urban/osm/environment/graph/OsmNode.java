package smartgov.urban.osm.environment.graph;

import org.locationtech.jts.geom.Coordinate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import smartgov.urban.geo.environment.graph.GeoNode;

/**
 * Class used to represents OSM nodes, i.e. crossroads and line breaks.
 * 
 * 
 * @author pbreugnot
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmNode extends GeoNode {

	// TODO : serialize as id?
	private Road road;
	
	public OsmNode(String id, Coordinate coordinate, Road road) {
		super(id, coordinate);
		this.road = road;
	}
	
	@JsonCreator
	public OsmNode(
		@JsonProperty("id") String id,
		@JsonProperty("lat") double lat,
		@JsonProperty("lon") double lon) {
		super(id, new Coordinate(lon, lat));
	}

	public Road getRoad() {
		return road;
	}

}
