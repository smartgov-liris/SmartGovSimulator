package smartgov.urban.osm.agent;

import java.util.Random;

import smartgov.SmartGov;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.graph.Node;
import smartgov.urban.geo.agent.GeoAgent;
import smartgov.urban.osm.agent.behavior.RandomTrafficBehavior;
import smartgov.urban.osm.environment.OsmContext;

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
			MovingBehavior basicBehavior) {
		super(id, body, basicBehavior);
		body.initialize();
	}
	
	public static OsmAgent randomTrafficOsmAgent(String id, OsmContext context, OsmAgentBody body) {
		// TODO : ugly, does the same thing that refresh(). Initialization must be improved.
		Random rnd = new Random();
		Node randomOrigin = RandomTrafficBehavior.selectRandomSourceNode(rnd, (OsmContext) context);
		Node randomDestination = RandomTrafficBehavior.selectRandomSinkNode(
				rnd,
				context.getSourceNodes().get(randomOrigin.getId()),
				(OsmContext) context);
		while (randomDestination == null) {
			((OsmContext) context).getSourceNodes().remove(randomOrigin.getId());
			SmartGov.logger.info("Removing useless source node " + randomOrigin.getId());
			randomOrigin = RandomTrafficBehavior.selectRandomSourceNode(rnd, (OsmContext) context);
			randomDestination = RandomTrafficBehavior.selectRandomSinkNode(
					rnd,
					context.getSourceNodes().get(randomOrigin.getId()),
					(OsmContext) context);
		}
		
		OsmAgent newAgent = new OsmAgent(
				id,
				body,
				new RandomTrafficBehavior(
						body,
						randomOrigin,
						randomDestination,
						(OsmContext) context));

//		body.initialize();
		return newAgent;
	}
	
//	/**
//	 * Refresh the behavior and initialize the osmAgentBody.
//	 */
//	public void initialize() {
//		// ((RandomTrafficBehavior) getBehavior()).refresh();
//		((OsmAgentBody) getBody()).initialize();
//	}
}
