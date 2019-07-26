package smartgov.urban.osm.agent.mover;

import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.mover.BasicGeoMover;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.Road;

/**
 * Used to move an OsmAgentBody in the osm graph, as if it were
 * driving a car.
 *
 * <p>
 * Event handlers are added on agent body to properly handle osm
 * road changes on arc reached.
 * </p>
 * 
 * <p>
 * Cars move according to a <a
 * href="https://en.wikipedia.org/wiki/Gipps%27_model">Gipps Model</a>.
 * </p>
 * 
 * @author pbreugnot
 *
 */
public class CarMover extends BasicGeoMover {

	private GippsSteering gippsSteering;
	private double maximumSpeed;
	private double vehicleSize;

	/**
	 * CarMover constructor. Parameters are those used in the Gipps' model.
	 * 
	 * @param maximumAcceleration maximum car acceleration (m/s-2)
	 * @param maximumBraking <b>negative</b>, maximum car braking (m/s-2)
	 * @param maximumSpeed maximum speed at which the agent should travel (m/s-1)
	 * @param vehicleSize vehicle size, that includes any security margin
	 */
	public CarMover(double maximumAcceleration, double maximumBraking, double maximumSpeed, double vehicleSize) {
		gippsSteering = new GippsSteering(maximumAcceleration, maximumBraking);
		this.maximumSpeed = maximumSpeed;
		this.vehicleSize = vehicleSize;
	}
	
	/**
	 * Gipps' model instance of this mover.
	 *
	 * @return Gipps' model
	 */
	public GippsSteering getGippsSteering() {
		return gippsSteering;
	}
	
	

	public double getMaximumSpeed() {
		return maximumSpeed;
	}

	public double getVehicleSize() {
		return vehicleSize;
	}
	
	/**
	 * Sets the agent body associated to this mover, and
	 * register additional event handlers (onArcReached)
	 * to handle road changes.
	 * 
	 * @param agentBody associated agent body
	 */
	@Override
	public void setAgentBody(GeoAgentBody agentBody) {
		super.setAgentBody(agentBody);
		registerArcReachedListener();
	}
	
	/*
	 * Handles road changes
	 */
	private void registerArcReachedListener() {
		agentBody.addOnArcReachedListener((event) -> {
			Road currentRoad = ((OsmAgentBody) agentBody).getCurrentRoad();
			Road newRoad = ((OsmArc) event.getArc()).getRoad();
			
			if(currentRoad == null) {
				newRoad.addAgent((OsmAgentBody) agentBody);
			}
			else {
				if(currentRoad != newRoad) {
					currentRoad.removeAgent((OsmAgentBody) agentBody);
					newRoad.addAgent((OsmAgentBody) agentBody);
				}
				else {
					Boolean directionChanged = false;
					switch(((OsmArc) event.getArc()).getRoadDirection()) {
					case BACKWARD:
						if(currentRoad.getForwardAgents().contains(agentBody)) {
							directionChanged = true;
						}
						break;
					case FORWARD:
						if(currentRoad.getBackwardAgents().contains(agentBody)) {
							directionChanged = true;
						}
						break;
					default:
						break;
					
					}
					if (directionChanged) {
						// Same road, but changed direction
						currentRoad.removeAgent((OsmAgentBody) agentBody);
						newRoad.addAgent((OsmAgentBody) agentBody);
					}
				}
			}
	});
	}

	@Override
	protected void updateAgentSpeed(GeoAgentBody agentBody) {
		Road road = ((OsmArc) agentBody.getPlan().getCurrentArc()).getRoad();
		OsmAgentBody leader = null;

		leader = road.leaderOfAgent(agentBody);
		
		if (leader != null) {
			agentBody.setSpeed(
					gippsSteering.getSpeed(
						agentBody.getSpeed(), // current speed
						maximumSpeed, // Max speed
						leader.getSpeed(),
						((CarMover) leader.getMover()).getVehicleSize(),
						((CarMover) leader.getMover()).getGippsSteering().getMaxBraking(),
						road.distanceBetweenTwoAgents(leader, (OsmAgentBody) agentBody)
						)
					);
		}
		else {
			agentBody.setSpeed(
					gippsSteering.getSpeedWithoutLeader(
						agentBody.getSpeed(),
						maximumSpeed)
					);
		}
	}
}
