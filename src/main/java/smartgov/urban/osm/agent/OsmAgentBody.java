package smartgov.urban.osm.agent;


import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.agent.mover.CarMover;

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
	
	/**
	 * OsmAgentBody constructor.
	 * 
	 * @param carMover A CarMover
	 */
	public OsmAgentBody(CarMover carMover) {
		super(carMover);
		carMover.setAgentBody(this);
	}
	
	/**
	 * Updates the agent plan from the origin and destination of the basic
	 * behavior, and make it spawn on the origin source node.
	 */
	public void initialize() {
//		updatePlan(
//			context.getGraph().shortestPath(
//					((RandomTrafficBehavior) getAgent().getBehavior()).getOrigin(),
//					((RandomTrafficBehavior) getAgent().getBehavior()).getDestination()
//					)
//			);

		// Make the current agent available for the source node.
//		context.agentsStock.get(
//				((RandomTrafficBehavior) getAgent().getBehavior()).getOrigin().getId()
//				).add(getAgent());
		
		// Set up body position
		setPosition(((GeoNode) ((MovingBehavior) getAgent().getBehavior()).getOrigin()).getPosition());
	}
	
}
