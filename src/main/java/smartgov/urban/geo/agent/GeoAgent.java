package smartgov.urban.geo.agent;

import smartgov.core.agent.MovingAgent;
import smartgov.core.agent.behavior.MovingBehavior;

/**
 * A generic {@link smartgov.core.agent.Agent AbstractAgent} used to represent an agent in a
 * geographical environment.
 * 
 * If you want to use an agent that live in an OSM environment, please consider using directly an
 * {@link smartgov.urban.osm.agent.OsmAgent OsmAgent} instead.
 * 
 * @author pbreugnot
 *
 * @param <Tnode> GeoNode type
 * @param <Tarc> GeoArc type
 * @param <Tbody> GeoAgentBody type
 * @param <C> CommuterPerception
 * @param <P> Properties associated to the GeoAgent bahavior.
 * @param <B> GeoAgent behavior
 */
public abstract class GeoAgent extends MovingAgent {
	
	public GeoAgent(
			String id,
			GeoAgentBody body,
			MovingBehavior behavior) {
		super(id, body, behavior);
		body.setSpeed(0.0);
		this.behavior = behavior;
	}
	
	/**
	 * Called each time an agent spawns as a new agents.
	 *
	 * Will perform :
	 * <ol>
	 * 	<li> Agent properties initialization : initialize origin and destination.
	 *  <li> Agent behavior initialization, that can depend on previously initialized properties.
	 *  <li> Agent body initialization : can depend on previously initialized properties and 
	 *  behavior. Will for example update agent body's Plan according to the new origin and destination..
	 * </ol>
	 */
	@Override
	public void initialize() {
		body.initialize();
	}
}
