package smartgov.urban.osm.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class OsmAgent<B extends AbstractBehavior> extends GeoAgent<OsmNode, OsmArc, B> {

	@JsonProperty("properties")
	private OsmAgentProperties agentProperties;
	
	public OsmAgent(
			String id,
			OsmAgentBody body,
			B basicBehavior,
			OsmAgentProperties agentProperties) {
		super(id, body, basicBehavior);
		this.agentProperties = agentProperties;
		
		// TODO: This can easily be forgotten in other implementing classes...
		body.setAgent(this);
	}
	
	public OsmAgentProperties getAgentProperties() {
		return agentProperties;
	}
	
	@Override
	public void initialize() {
		agentProperties.initialize();
		super.initialize();
	}
}