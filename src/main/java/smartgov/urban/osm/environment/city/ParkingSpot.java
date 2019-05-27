package smartgov.urban.osm.environment.city;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.environment.ActionableByHumanAgent;
import smartgov.core.environment.LowLevelAction;
import smartgov.core.environment.Perceivable;
import smartgov.urban.geo.environment.graph.EdgeSpot;
import smartgov.urban.osm.environment.OsmContext;


/**
 * ParkingSpot is an atomic element of a parking or block face. It only knows its
 * status witch is occupied or not.
 * @author spageaud
 *
 */
public class ParkingSpot extends EdgeSpot implements ActionableByHumanAgent, Perceivable {
	
	protected OsmContext environment;

	protected boolean occupied;
	protected String idOfAgentParked = "-1";
	protected String idNode;
	
	//If no roads come near this spot
	private boolean failed = false;
	private boolean unavailable = false;
	
	public ParkingSpot(
			OsmContext environment,
			Coordinate position,
			boolean isOccupied,
			String id) {
		super(position);
		this.environment = environment;
		this.occupied = isOccupied;
	}	

	@Override
	public List<LowLevelAction> getAvailableHumanActions() {
		List<LowLevelAction> availableActions = new ArrayList<>();
		if(occupied){
			availableActions.add(LowLevelAction.LEAVE);
		} else {
			availableActions.add(LowLevelAction.ENTER);
		}
		return availableActions;
	}

	@Override
	public void doHumanAction(LowLevelAction action) {

	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public void setIdNode(String idNode) {
		this.idNode = idNode;
	}
	
	public String getIdNode() {
		return idNode;
	}
	
	public void setOccupied(boolean isOccupied) {
		this.occupied = isOccupied;
	}
	
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	
	public boolean isFailed() {
		return failed;
	}
	
	public boolean isUnavailable() {
		return unavailable;
	}
	
	public void setUnavailable(boolean unavailable) {
		this.unavailable = unavailable;
	}
	
	public void setIdOfAgentParked(String id) {
		this.idOfAgentParked = id;
	}
	
	public String getIdOfAgentAsInt() {
		//TODO remove
		return idOfAgentParked;
	}

}
