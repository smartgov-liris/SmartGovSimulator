package smartgov.urban.osm.environment.graph;

import java.util.Arrays;

import org.locationtech.jts.geom.Coordinate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.output.RoadIdSerializer;

/**
 * Class used to represents OSM nodes, i.e. crossroads and line breaks.
 * 
 * @author pbreugnot
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmNode extends GeoNode {

	// TODO : serialize as id?
	@JsonSerialize(using = RoadIdSerializer.class)
	private Road road;
	
	/**
	 * OsmNode constructor. Can be used to instantiate manually an osm node
	 * with the given id at the specified coordinates.
	 *
	 * <p>
	 * If the node as nothing to do with OSM data, you should better use
	 * directly a
	 * {@link smartgov.urban.geo.environment.graph.GeoNode GeoNode}.
	 * </p>
	 *
	 * @param id node id
	 * @param coordinate node coordinates in longitude / latitude
	 */
	public OsmNode(String id, Coordinate coordinate) {
		super(id, coordinate);
	}
	
	/**
	 * A JsonCreator to loas osm nodes from json files. Can also be used as
	 * a normal constructor.
	 *
	 * @param id node id
	 * @param lat node latitude
	 * @param lon node longitude
	 */
	@JsonCreator
	public OsmNode(
		@JsonProperty("id") String id,
		@JsonProperty("lat") double lat,
		@JsonProperty("lon") double lon) {
		super(id, new Coordinate(lon, lat));
	}
	
	/**
	 * Sets the road to which this node belongs to.
	 *
	 * @param road osm road
	 */
	public void setRoad(Road road) {
		this.road = road;
	}

	/**
	 * Road to which this node belongs to. Might be null.
	 *
	 * @return osm road
	 */
	public Road getRoad() {
		return road;
	}

	@Override
	public String toString() {
		return "OsmNode [road=" + road + ", id=" + id + ", position="
				+ Arrays.toString(getPositionAsArray()) + "]";
	}
	
	

}
