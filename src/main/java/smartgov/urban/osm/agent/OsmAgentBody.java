package smartgov.urban.osm.agent;


import smartgov.core.agent.moving.ParkingArea;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.agent.mover.CarMover;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;

/**
 * An agent body moving in the OSM graph, as in a vehicle.
 *
 * This agent can move between {@link smartgov.urban.osm.environment.graph.OsmNode OsmNodes} on
 * {@link smartgov.urban.osm.environment.graph.OsmArc OsmArcs} using the {@link smartgov.urban.osm.agent.mover.CarMover CarMover}.
 * 
 * @author pbreugnot
 *
 */
public class OsmAgentBody extends GeoAgentBody {
	
	private Road currentRoad;
	
	/**
	 * OsmAgentBody constructor.
	 * 
	 * @param carMover A CarMover
	 */
	public OsmAgentBody(CarMover carMover) {
		super(carMover);
		carMover.setAgentBody(this);
	}
	
	public Road getCurrentRoad() {
		return currentRoad;
	}

	public void setCurrentRoad(Road currentRoad) {
		this.currentRoad = currentRoad;
	}
	
	@Override
	public void handleEnter(ParkingArea parkingArea) {
		super.handleEnter(parkingArea);
		((OsmNode) getPlan().getCurrentNode()).getRoad().removeAgent(this);
	}
	
	@Override
	public void handleLeave(ParkingArea parkingArea) {
		super.handleLeave(parkingArea);
		((OsmNode) getPlan().getCurrentNode()).getRoad().addAgent(this);
	}

	/**
	 * Updates the agent plan from the origin and destination of the basic
	 * behavior, and make it spawn on the origin source node.
	 * <p>
	 * Also adds it to the new road, according to the behavior origin.
	 * </p>
	 */
	public void initialize() {
		// Set up body position
		setPosition(((GeoNode) getPlan().getCurrentNode()).getPosition());
		
		// Adds the agent to the new road
		Road currentRoad = ((OsmArc) getPlan().getCurrentArc()).getRoad();
		currentRoad.addAgent(this);
		setCurrentRoad(currentRoad);
		
	}
	
}
