package smartgov.urban.osm.agent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import smartgov.core.environment.SmartGovContext;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.agent.actuator.CarMover;
import smartgov.urban.osm.agent.behavior.BasicBehavior;

/**
 * An OSM implementation of a {@link smartgov.urban.geo.agent.GeoAgentBody GeoAgentBody}.
 * This agent can move between {@link smartgov.urban.osm.environment.graph.OsmNode OsmNodes} on
 * {@link smartgov.urban.osm.environment.graph.OsmArc OsmArcs} using the {@link smartgov.urban.osm.agent.actuator.CarMover CarMover}.
 * It senses data from the environment using a {@link smartgov.urban.osm.agent.perception.CarDriverSensor}.
 * 
 * @author pbreugnot
 *
 */
public class OsmAgentBody extends GeoAgentBody {

	@JsonIgnore
	private SmartGovContext environment;
	
	/**
	 * OsmAgentBody constructor.
	 * 
	 * @param id AgentBody id
	 * @param geography Current geography
	 * @param sensor A CarDriverSensor
	 * @param carMover A CarMover
	 * @param environment Current OsmEnvironment
	 */
	public OsmAgentBody(
			CarMover carMover,
			SmartGovContext environment) {
		super(carMover);
		carMover.setAgentBody(this);
		this.environment = environment;
	}
	
	/**
	 * Set up agent plan with begin and start node previously set up in properties.
	 * 
	 * @see smartgov.urban.osm.agent.properties.OsmAgentProperties#initialize()
	 */
	public void initialize() {
		updatePlan(
			environment.graph.shortestPath(
					((BasicBehavior) agent.getBehavior()).getOrigin(),
					((BasicBehavior) agent.getBehavior()).getDestination()
					)
			);

		// Make the current agent available for the source node.
		environment.agentsStock.get(
				((BasicBehavior) agent.getBehavior()).getOrigin().getId()
				).add(agent);
		
		// Set up body position
		setPosition(((GeoNode) ((BasicBehavior) agent.getBehavior()).getOrigin()).getPosition());
	}
	
}