package smartgov.urban.osm.agent;

import smartgov.urban.geo.agent.GeoAgent;
import smartgov.urban.osm.agent.behavior.BasicBehavior;

/**
 * OSM implementation of a {@link smartgov.urban.geo.agent.GeoAgent GeoAgent}.
 * 
 * @author pbreugnot
 *
 */
public class OsmAgent extends GeoAgent {

	
	public OsmAgent(
			String id,
			OsmAgentBody body,
			BasicBehavior basicBehavior) {
		super(id, body, basicBehavior);
	}
	
	public void initialize() {
		((BasicBehavior) getBehavior()).refresh();
		((OsmAgentBody) getBody()).initialize();
	}
}