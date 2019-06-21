package smartgov.urban.osm.agent.actuator;

import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.mover.BasicGeoMover;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.OsmArc;

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

	private GippsSteering gippsSteering;

	/**
	 * CarMover constructor.
	 */
	public CarMover() {
		gippsSteering = new GippsSteering(1.0,1.5,-3.0);
	}
	
	/**
	 * Gipps' model instance of this mover.
	 *
	 * @return Gipps' model
	 */
	public GippsSteering getSteering() {
		return gippsSteering;
	}

	@Override
	protected void handleArcChanged(GeoArc oldArc, GeoArc newArc) {
		((OsmArc) oldArc).getRoad().getAgentsOnPath().remove(agentBody);
		if (!((OsmArc) newArc).getRoad().getAgentsOnPath().contains(agentBody)) {
			((OsmArc) newArc).getRoad().getAgentsOnPath().add((OsmAgentBody) agentBody);
		}
		agentBody.setDirection(newArc.getDirection());
		
	}

	@Override
	protected void updateAgentSpeed(GeoAgentBody agentBody) {
		OsmAgentBody leader = ((OsmArc) agentBody.getPlan().getCurrentArc()).getRoad().getLeaderOfAgent(agentBody);
		if (leader != null) {
			agentBody.setSpeed(
					gippsSteering.getSpeed(
						agentBody.getSpeed(), // current speed
						40, // Max speed
						leader.getPosition(), // Position of the leader
						leader.getSpeed(),
						7.5,
						-3,
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
	
//	/**
//	 * Adds a new CarMovedEvent handler.
//	 *
//	 * Triggered each time the car moves : can be triggered several times
//	 * in the same tick, depending on the tick duration and the agent
//	 * speed.
//	 *
//	 * @param listener new car moved event handler to add
//	 */
//	public void addCarMovedEventListener(EventHandler<CarMovedEvent> listener) {
//		carMovedListeners.add(listener);
//	}
//	
//	private void triggerCarMovedListeners(CarMovedEvent event) {
//		for (EventHandler<CarMovedEvent> listener : carMovedListeners) {
//			listener.handle(event);
//		}
//	}
}
