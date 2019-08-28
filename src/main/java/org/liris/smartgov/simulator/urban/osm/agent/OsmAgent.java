package org.liris.smartgov.simulator.urban.osm.agent;

import java.util.Random;

import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MovingBehavior;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.urban.geo.agent.GeoAgent;
import org.liris.smartgov.simulator.urban.osm.agent.behavior.RandomTrafficBehavior;
import org.liris.smartgov.simulator.urban.osm.environment.OsmContext;

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
	}
	
	/**
	 * Creates an agent that will spawn on a random source node from the current context
	 * and go to a random sink node, using the shortest path.
	 * When the agent reaches its destination, it spawns on a new random
	 * source node and so on, as specified by the
	 * {@link org.liris.smartgov.simulator.urban.osm.agent.behavior.RandomTrafficBehavior}.
	 * 
	 * @param id agent id
	 * @param context current context, containing source and sink nodes
	 * @param body initialized osm agent body
	 * @return a random traffic agent
	 */
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

		return newAgent;
	}
}
