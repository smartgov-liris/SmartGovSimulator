package smartgov.urban.osm.agent.mover;

import java.util.List;

import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.mover.BasicGeoMover;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.Road;

/**
 * Used to move an OsmAgentBody in the osm graph, as if it were
 * driving a car.
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
	
	private static final double reactionTime = 1.0;

	private GippsSteering gippsSteering;
	private double maximumSpeed;
	private double vehicleSize;

	/**
	 * CarMover constructor.
	 */
	public CarMover(double maximumAcceleration, double maximumBraking, double maximumSpeed, double vehicleSize) {
		gippsSteering = new GippsSteering(reactionTime, maximumAcceleration, maximumBraking);
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

	@Override
	protected void handleArcChanged(GeoArc oldArc, GeoArc newArc) {
		if (((OsmArc) oldArc).getRoad() != ((OsmArc) newArc).getRoad()) {
			/*
			 * Removes agent from the old road
			 */
			List<OsmAgentBody> agentsOnOldRoad = null;
			switch(((OsmArc) oldArc).getRoadDirection()) {
			case BACKWARD:
				agentsOnOldRoad = ((OsmArc) oldArc).getRoad().getBackwardAgentsOnRoad();
				break;
			case FORWARD:
				agentsOnOldRoad = ((OsmArc) oldArc).getRoad().getForwardAgentsOnRoad();
				break;
			default:
				break;
			
			}
			agentsOnOldRoad.remove(agentBody);
			

			List<OsmAgentBody> agentsOnNewRoad = null;
			switch(((OsmArc) newArc).getRoadDirection()) {
			case BACKWARD:
				agentsOnNewRoad = ((OsmArc) newArc).getRoad().getBackwardAgentsOnRoad();
				break;
			case FORWARD:
				agentsOnNewRoad = ((OsmArc) newArc).getRoad().getForwardAgentsOnRoad();
				break;
			default:
				break;
			
			}
			
			agentsOnNewRoad.add((OsmAgentBody) agentBody);
			
			agentBody.setDirection(newArc.getDirection());
		}
	}

	@Override
	protected void updateAgentSpeed(GeoAgentBody agentBody) {
		Road road = ((OsmArc) agentBody.getPlan().getCurrentArc()).getRoad();
		OsmAgentBody leader = null;
		switch(((OsmArc) agentBody.getPlan().getCurrentArc()).getRoadDirection()) {
		case BACKWARD:
			leader = road.backwardLeaderOfAgent(agentBody);
			break;
		case FORWARD:
			leader = road.forwardLeaderOfAgent(agentBody);
			break;
		default:
			break;
		
		}
		
		if (leader != null) {
			agentBody.setSpeed(
					gippsSteering.getSpeed(
						agentBody.getSpeed(), // current speed
						maximumSpeed, // Max speed
						leader.getPosition(), // Position of the leader
						leader.getSpeed(),
						((CarMover) leader.getMover()).getVehicleSize(),
						((CarMover) leader.getMover()).getGippsSteering().getMaximumBraking(),
						agentBody.getPosition())
					);
		}
		else {
			agentBody.setSpeed(
					gippsSteering.getSpeedWithoutLeader(
						agentBody.getSpeed(),
						40)
					);
		}
	}
}
