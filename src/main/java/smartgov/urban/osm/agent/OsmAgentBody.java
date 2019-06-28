package smartgov.urban.osm.agent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import smartgov.core.environment.SmartGovContext;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.agent.actuator.CarMover;
import smartgov.urban.osm.agent.behavior.RandomTrafficBehavior;
import smartgov.urban.osm.environment.OsmContext;

/**
 * An agent body moving in the OSM graph, as in a vehicle.
 *
 * This agent can move between {@link smartgov.urban.osm.environment.graph.OsmNode OsmNodes} on
 * {@link smartgov.urban.osm.environment.graph.OsmArc OsmArcs} using the {@link smartgov.urban.osm.agent.actuator.CarMover CarMover}.
 * 
 * @author pbreugnot
 *
 */
public class OsmAgentBody extends GeoAgentBody {

	@JsonIgnore
	private OsmContext context;
	
	/**
	 * OsmAgentBody constructor.
	 * 
	 * @param carMover A CarMover
	 * @param context osm context
	 */
	public OsmAgentBody(
			CarMover carMover,
			OsmContext context) {
		super(carMover);
		carMover.setAgentBody(this);
		this.context = context;
	}
	
	/**
	 * Updates the agent plan from the origin and destination of the basic
	 * behavior, and make it spawn on the origin source node.
	 */
	public void initialize() {
		updatePlan(
			context.getGraph().shortestPath(
					((RandomTrafficBehavior) getAgent().getBehavior()).getOrigin(),
					((RandomTrafficBehavior) getAgent().getBehavior()).getDestination()
					)
			);

		// Make the current agent available for the source node.
		context.agentsStock.get(
				((RandomTrafficBehavior) getAgent().getBehavior()).getOrigin().getId()
				).add(getAgent());
		
		// Set up body position
		setPosition(((GeoNode) ((RandomTrafficBehavior) getAgent().getBehavior()).getOrigin()).getPosition());
	}
	
}
