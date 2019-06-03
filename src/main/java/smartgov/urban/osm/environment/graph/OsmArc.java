package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.environment.city.ParkingSpot;

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
	
	protected Road road;
	protected String type;
	protected int lanes;
	protected List<ParkingSpot> spots;
	protected ParkingSpot closeSpot;
	
	/**
	 * OsmArc constructor.
	 * 
	 * @param id Arc id
	 * @param road Road to which the Arc belongs to.
	 * @param startNode Start Node
	 * @param targetNode Target Node
	 * @param lanes Number of OSM lanes
	 * @param type Arc type. (OSM 'highway' attribute)
	 */
	public OsmArc(
			String id,
			Road road,
			OsmNode startNode,
			OsmNode targetNode,
			int lanes,
			String type) {
		super(id, startNode, targetNode);
		this.road = road;
		spots = new ArrayList<>();
	}
	
	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road road) {
		this.road = road;
	}
	
	public String getType() {
		return type;
	}
	
	public List<ParkingSpot> getSpots() {
		return spots;
	}
	
	public boolean isASpotAvailable(){
		for(int i = 0; i < spots.size(); i++){
			if(!spots.get(i).isOccupied()){
				return true;
			}
		}
		return false;
	}
	
	public ParkingSpot getSpotCloseToNode(){
		for(int i = 0; i < spots.size(); i++){
			if(GISComputation.GPS2Meter(((GeoNode) getStartNode()).getPosition(), spots.get(i).getPosition()) < 25.0){
				return spots.get(i);
			}
		}
		return null;
	}
	
	public void setSpots(List<ParkingSpot> spots) {
		this.spots = spots;
	}
	
	public void addParking(ParkingSpot spot){
		this.spots.add(spot);
	}
	
}
