package smartgov.urban.geo.agent;

import smartgov.core.agent.AbstractAgent;
import smartgov.core.agent.behavior.AbstractBehavior;
import smartgov.core.agent.properties.AgentProperties;
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
public abstract class GeoAgent<Tnode extends GeoNode<Tarc>, Tarc extends GeoArc<Tnode>, Tbody extends GeoAgentBody<?, ?, ?, ?>, B extends AbstractBehavior<Tbody>> extends AbstractAgent<Tbody> {

	private B behavior;
	
	public GeoAgent(
			String id,
			Tbody body,
			B behavior,
			AgentProperties agentProperties) {
		super(id, body, agentProperties);
		this.body.setSpeed(0.0);
		this.behavior = behavior;
	}

//	@Override
	public void live() {
//		perceptions.filterPerception(this.getBody().getPerception());
		this.getBody().doAction(behavior.provideAction());
//		perceptions.clear();
	}
	
//	public AbstractProperties getProperties() {
//		return properties;
//	}
//	
//	public AbstractBehavior<? extends AbstractPerception, ? extends AbstractProperties, ? extends AbstractAgentBody<?, ?, ?>> getBehavior() {
//		return behavior;
//	}
	
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
		agentProperties.initialize();
		behavior.initialize();
		body.initialize();
	}

// TODO : This might be replaced by initialize. Need to check for each scenario.
//	@Override
//	public void recycleAgent(int id) {
//		this.setId(id);
//		this.getProperties().resetProperties(id);
//		this.behavior.setInitialState();
//		if(this.body != null){
//			this.body.init();
//			this.body.setAgent(this);
//		}
//		
//	}
}
