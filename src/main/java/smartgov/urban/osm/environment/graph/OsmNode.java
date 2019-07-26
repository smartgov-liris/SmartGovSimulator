package smartgov.urban.osm.environment.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.utils.LatLon;
import smartgov.urban.osm.output.RoadIdSerializer;

/**
 * Class used to represents OSM nodes.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmNode extends GeoNode {

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
	public OsmNode(String id, LatLon coordinate) {
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
		super(id, new LatLon(lat, lon));
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
	 * Road to which this node belongs to.
	 * <p>
	 * Notice that the road is automatically set by the
	 * {@link smartgov.urban.osm.utils.OsmArcsBuilder}.
	 * However, for nodes representing <b>the junction
	 * between two roads</b>, which road is associated 
	 * to the node is uncertain, because it depends on
	 * the order in which roads are read.
	 * </p>
	 * 
	 * <p>
	 * However, the road associated to an arc is unambiguous,
	 * so use arcs instead if it's really important. Arcs can be accessed
	 * through {@link #getIncomingArcs()}, {@link #getOutgoingArcs()},
	 * or event from {@link smartgov.core.agent.moving.plan.Plan#getCurrentArc()}.
	 * </p>
	 *
	 * @return osm road associated to this node
	 */
	public Road getRoad() {
		return road;
	}

	@Override
	public String toString() {
		return "OsmNode [road=" + road + ", id=" + id + ", position=["
				+ getPosition().lat + ", " + getPosition().lon + "]";
	}
	
	

}
