package smartgov.urban.osm.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

import smartgov.urban.geo.agent.GeoAgent;
import smartgov.urban.osm.agent.behavior.BasicBehavior;
import smartgov.urban.osm.agent.properties.OsmAgentProperties;

/**
 * OSM implementation of a {@link smartgov.urban.geo.agent.GeoAgent GeoAgent}.
 * 
 * @author pbreugnot
 *
 */
public class OsmAgent extends GeoAgent {

	@JsonProperty("properties")
	private OsmAgentProperties agentProperties;
	
	public OsmAgent(
			String id,
			OsmAgentBody body,
			BasicBehavior basicBehavior,
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