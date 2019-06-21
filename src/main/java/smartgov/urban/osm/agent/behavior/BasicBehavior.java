package smartgov.urban.osm.agent.behavior;

import java.util.Random;

import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.graph.Node;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.AbstractOsmSinkSourceNode;

/**
 * A basic behavior that describes agent moving from a random source node to a
 * random sink node by the shortest path, performing only MOVE actions.
 */
public class BasicBehavior extends MovingBehavior {
	
	/**
	 * BasicBehavior constructor.
	 *
	 * @param agentBody osm agent body involved in the behavior
	 * @param origin initial origin
	 * @param destination initial destination
	 * @param context osm context
	 */
	public BasicBehavior(OsmAgentBody agentBody, AbstractOsmSinkSourceNode origin, AbstractOsmSinkSourceNode destination, OsmContext context) {
		super(agentBody, origin, destination, context);
	}
	
	/**
	 * Refresh the behavior with a random source and sink node.
	 */
	public void refresh() {
		Random rnd = new Random();
		Node origin = selectRandomSourceNode(rnd,(OsmContext) getContext());
		Node destination = selectRandomSinkNode(rnd, origin,(OsmContext) getContext());
		refresh(origin, destination);
	}
	
	/**
	 * Select a random source node from those available in the osm context.
	 *
	 * @param rnd Random instance
	 * @param context osm context
	 */
	public static AbstractOsmSinkSourceNode selectRandomSourceNode(Random rnd, OsmContext context) {
		String randomSourceNodeId = (String) context.getSourceNodes().keySet().toArray()[rnd.nextInt(context.getSourceNodes().size())];
		return (AbstractOsmSinkSourceNode) context.getSourceNodes().get(randomSourceNodeId);
	}
	

	/**
	 * Select a random sink node from those available in the osm context,
	 * different from the specified source node.
	 *
	 * @param rnd Random instance
	 * @param sourceNode previously selected source node
	 * @param context osm context
	 */
	public static AbstractOsmSinkSourceNode selectRandomSinkNode(Random rnd, Node sourceNode, OsmContext context) {
		String randomSinkNodeId = (String) context.getSinkNodes().keySet().toArray()[rnd.nextInt(context.getSinkNodes().size())];
		while(randomSinkNodeId == sourceNode.getId()) {
			randomSinkNodeId = (String) context.getSinkNodes().keySet().toArray()[rnd.nextInt(context.getSinkNodes().size())];
		}
		return (AbstractOsmSinkSourceNode) context.getSinkNodes().get(randomSinkNodeId);
	}
	
	/**
	 * Always MOVE.
	 */
	@Override
	public MoverAction provideAction() {
		return MoverAction.MOVE();
	}
}
