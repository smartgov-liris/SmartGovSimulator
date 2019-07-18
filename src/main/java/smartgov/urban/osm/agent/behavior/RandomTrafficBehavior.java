package smartgov.urban.osm.agent.behavior;

import java.util.Random;

import smartgov.SmartGov;
import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.graph.Node;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SourceNode;

/**
 * A basic behavior that describes agent moving from a random source node to a
 * random sink node by the shortest path, performing only MOVE actions.
 */
public class RandomTrafficBehavior extends MovingBehavior {
	
	/**
	 * BasicBehavior constructor.
	 *
	 * @param agentBody osm agent body involved in the behavior
	 * @param origin initial origin
	 * @param destination initial destination
	 * @param context osm context
	 */
	public RandomTrafficBehavior(OsmAgentBody agentBody, Node origin, Node destination, OsmContext context) {
		super(agentBody, origin, destination, context);
		/*
		 * Notice that agents are removed from road in the CarMover, when the last arc has been left.
		 */
		agentBody.addOnDestinationReachedListener((event) -> {
				refresh(); // Pick a new source / destination
				agentBody.initialize(); // Set up agent body position
			});
	}
	
	/**
	 * Refresh the behavior with a random source and sink node.
	 */
	public void refresh() {
		Random rnd = new Random();
		Node origin = selectRandomSourceNode(rnd,(OsmContext) getContext());
		Node destination = selectRandomSinkNode(
				rnd,
				((OsmContext) getContext()).getSourceNodes().get(origin.getId()),
				(OsmContext) getContext());
		if (destination == null) {
			((OsmContext) getContext()).getSourceNodes().remove(origin.getId());
			SmartGov.logger.info("Removing useless source node " + origin.getId());
			refresh();
			return;
		}
		refresh(origin, destination);
	}
	
	/**
	 * Select a random source node from those available in the osm context.
	 *
	 * @param rnd Random instance
	 * @param context osm context
	 * @return a random source node, extracted from the specified context
	 */
	public static Node selectRandomSourceNode(Random rnd, OsmContext context) {
		String randomSourceNodeId = (String) context.getSourceNodes().keySet().toArray()[rnd.nextInt(context.getSourceNodes().size())];
		return context.nodes.get(context.getSourceNodes().get(randomSourceNodeId).getNodeId());
	}
	

	/**
	 * Select a random sink node from those available in the osm context,
	 * extracted from the possible destinations of the specified source node.
	 *
	 * @param rnd Random instance
	 * @param sourceNode previously selected source node
	 * @param context osm context
	 * @return a random sink node, extracted from the possible destinations of the specified source node
	 */
	public static Node selectRandomSinkNode(Random rnd, SourceNode sourceNode, OsmContext context) {
		if(sourceNode.destinations().size() > 0) {
			SinkNode randomSinkNode = (SinkNode) sourceNode.destinations().toArray()[rnd.nextInt(sourceNode.destinations().size())];
			try {
				context.getGraph().shortestPath(context.nodes.get(sourceNode.getNodeId()), context.nodes.get(randomSinkNode.getNodeId()));
			}
			catch (IllegalArgumentException e) {
				sourceNode.destinations().remove(randomSinkNode);
				// No path available between the two nodes
				return selectRandomSinkNode(rnd, sourceNode, context);
			}
			return context.nodes.get(randomSinkNode.getNodeId());
		}
		return null;
	}
	
	/**
	 * Always MOVE.
	 */
	@Override
	public MoverAction provideAction() {
		return MoverAction.MOVE();
	}
}
