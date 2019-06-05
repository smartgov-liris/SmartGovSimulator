package smartgov.urban.geo.agent;

import smartgov.core.agent.moving.MovingAgent;
import smartgov.core.agent.moving.behavior.MovingBehavior;

/**
 * A generic {@link smartgov.core.agent.core.Agent AbstractAgent} used to represent an agent in a
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
}
