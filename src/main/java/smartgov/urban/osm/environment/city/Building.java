package smartgov.urban.osm.environment.city;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;

import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;

/**
 * Abstract Building class.
 * 
 * @author pbreugnot
 *
 */
public abstract class Building extends GeoNode<GeoArc<?>> {
	
	@SuppressWarnings("serial")
	public static List<String> OFFICE_TYPE = new ArrayList<String>(){
		{
			add("retail");
			add("industrial");
			add("factory");
		}
	};
	
	public List<? extends GeoNode<?>> closestNodesWithSpots;
	private Map<String, String> osmTags;
	private BuildingType type;
	private String closestNodeId;
	
	public Building(String id, Map<String, String> attributes, Coordinate[] polygon) {
		super(id, polygon[0]);
		this.osmTags = attributes;
	}
	
	public String getClosestNodeId() {
		return this.closestNodeId;
	}
	
	public Map<String, String> getOsmTags() {
		return osmTags;
	}
	
	public void setOsmTags(Map<String, String> osmTags) {
		this.osmTags = osmTags;
	}
	
	public BuildingType getType() {
		return type;
	}
	
	public void setType(BuildingType type) {
		this.type = type;
	}
	
	public GeoNode<?> getClosestNode(Map<String, GeoNode<?>> nodes){
		// return OsmBuilder.environment.nodes.get(closestNodeId);
		return nodes.get(closestNodeId);
	}
	
	public void setClosestNodesWithSpots(List<? extends GeoNode<?>> closestNodesWithSpots) {
		this.closestNodesWithSpots = closestNodesWithSpots;
	}
	
	public List<? extends GeoNode<?>> getClosestNodesWithSpots() {
		return closestNodesWithSpots;
	}

}
