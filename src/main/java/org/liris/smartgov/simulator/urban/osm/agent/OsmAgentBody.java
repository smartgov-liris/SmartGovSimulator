package org.liris.smartgov.simulator.urban.osm.agent;


import org.liris.smartgov.simulator.core.agent.moving.ParkingArea;
import org.liris.smartgov.simulator.urban.geo.agent.GeoAgentBody;
import org.liris.smartgov.simulator.urban.osm.agent.mover.CarMover;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;

/**
 * An agent body moving in the OSM graph, as in a vehicle.
 *
 * This agent can move between {@link org.liris.smartgov.simulator.urban.osm.environment.graph.OsmNode OsmNodes} on
 * {@link org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc OsmArcs} using the {@link org.liris.smartgov.simulator.urban.osm.agent.mover.CarMover CarMover}.
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
	}
	
	/**
	 * Returns the current road this agent is moving on.
	 * 
	 * <p>
	 * Notice that if the agent has entered a parking area,
	 * it has been removed from the road, but the road can
	 * still be accessed from this function.
	 * </p>
	 * 
	 * @return road this agent is moving on
	 */
	public Road getCurrentRoad() {
		return currentRoad;
	}

	/**
	 * Sets the current road of this agents.
	 * 
	 * <p>
	 * Automatically called when {@link org.liris.smartgov.simulator.urban.osm.environment.graph.Road#addAgent}
	 * is called.
	 * </p>
	 * 
	 * @param currentRoad road this agent is moving on
	 */
	public void setCurrentRoad(Road currentRoad) {
		this.currentRoad = currentRoad;
	}
	
	/**
	 * Enters the specified parking area, and removes agent
	 * from the {@link #getCurrentRoad() current road}.
	 */
	@Override
	public void handleEnter(ParkingArea parkingArea) {
		super.handleEnter(parkingArea);
		currentRoad.removeAgent(this);
	}
	
	/**
	 * Leaves the specified parking area, and adds agent
	 * to the {@link #getCurrentRoad() current road}.
	 */
	@Override
	public void handleLeave(ParkingArea parkingArea) {
		super.handleLeave(parkingArea);
		currentRoad.addAgent(this);
	}
	
}
