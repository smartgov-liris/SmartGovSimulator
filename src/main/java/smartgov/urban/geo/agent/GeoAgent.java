package smartgov.urban.geo.agent;

import smartgov.core.agent.AbstractAgent;
import smartgov.core.agent.behavior.AbstractBehavior;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;

/**
 * A generic {@link smartgov.core.agent.AbstractAgent AbstractAgent} used to represent an agent in a
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
public abstract class GeoAgent<Tnode extends GeoNode, Tarc extends GeoArc, B extends AbstractBehavior> extends AbstractAgent {

	private B behavior;
	
	public GeoAgent(
			String id,
			GeoAgentBody<?> body,
			B behavior) {
		super(id, body);
		body.setSpeed(0.0);
		this.behavior = behavior;
	}

	@Override
	public void live() {
//		perceptions.filterPerception(this.getBody().getPerception());
		this.getBody().doAction(behavior.provideAction());
//		perceptions.clear();
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
		behavior.initialize();
		body.initialize();
	}
}
