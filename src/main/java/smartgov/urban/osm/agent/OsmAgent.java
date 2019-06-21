package smartgov.urban.osm.agent;

import smartgov.urban.geo.agent.GeoAgent;
import smartgov.urban.osm.agent.behavior.BasicBehavior;

/**
 * An agent that moves in the osm graph using a basic behavior.
 *
 * @author pbreugnot
 */
public class OsmAgent extends GeoAgent {

	
	/**
	 * OsmAgent constructor.
	 *
	 * @param id agent if
	 * @param body associated body
	 * @param basicBehavior basic behavior instance
	 */
	public OsmAgent(
			String id,
			OsmAgentBody body,
			BasicBehavior basicBehavior) {
		super(id, body, basicBehavior);
	}
	
	/**
	 * Refresh the behavior and initialize the osmAgentBody.
	 */
	public void initialize() {
		((BasicBehavior) getBehavior()).refresh();
		((OsmAgentBody) getBody()).initialize();
	}
}
