package smartgov.urban.osm.agent;

import smartgov.core.agent.behavior.AbstractBehavior;
import smartgov.urban.geo.agent.GeoAgent;
import smartgov.urban.osm.agent.properties.OsmAgentProperties;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;

/**
 * OSM implementation of a {@link smartgov.urban.geo.agent.GeoAgent GeoAgent}.
 * 
 * @author pbreugnot
 *
 * @param <P> Properties associated to the agent behavior.
 * @param <B> Agent behavior
 */
public class OsmAgent<P extends OsmAgentProperties, B extends AbstractBehavior<OsmAgentBody>> extends GeoAgent<OsmNode, OsmArc, OsmAgentBody, B> {

	public OsmAgent(
			String id,
			OsmAgentBody body,
			B basicBehavior,
			P properties) {
		super(id, body, basicBehavior, properties);
		// TODO: This can easily be forgotten in other implementing classes...
		body.setAgent(this);
	}

	
	
}