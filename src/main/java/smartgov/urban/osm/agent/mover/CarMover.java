package smartgov.urban.osm.agent.mover;

import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.mover.BasicGeoMover;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;
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
	
	public static final double reactionTime = 1.0;

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

			((OsmArc) oldArc).getRoad().removeAgent((OsmAgentBody) agentBody, ((OsmArc) oldArc).getRoadDirection());
			
			((OsmArc) newArc).getRoad().addAgent((OsmAgentBody) agentBody);
			
		}
		agentBody.setDirection(newArc.getDirection());
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
						maximumSpeed)
					);
		}
	}
	
	@Override
	protected void handleDestinationReached(GeoAgentBody agentBody, GeoArc lastArc, GeoNode lastNode) {
		/*
		 * Removes agent from the final road.
		 * At this point, we don't know if the origin of the next plan will be the same road...
		 * So we remove it in any case, and the agent will be added to the road again
		 * at behavior initialization.
		 */
		((OsmArc) lastArc).getRoad().removeAgent((OsmAgentBody) agentBody, ((OsmArc) lastArc).getRoadDirection());
	}
}
