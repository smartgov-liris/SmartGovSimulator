package smartgov.urban.osm.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Node;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.agent.mover.CarMover;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.environment.graph.factory.OsmArcFactory;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkSourceNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSourceNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SourceNode;
import smartgov.urban.osm.scenario.GenericOsmScenario;

/**
 * A scenario that demonstrates the usage of random traffic agents in an osm
 * graph.
 *
 * @see smartgov.urban.osm.agent.behavior.RandomTrafficBehavior
 */
public class RandomTrafficScenario<Tnode extends OsmNode, Troad extends Road> extends GenericOsmScenario<Tnode, Troad> {
	
	public RandomTrafficScenario(Class<Tnode> nodeClass, Class<Troad> roadClass, OsmArcFactory<?> osmArcFactory) {
		super(nodeClass, roadClass, osmArcFactory);
		// TODO Auto-generated constructor stub
	}

	public static final String name = "randomTraffic";
	

	/**
	 * Build a set of agents with a <i>random traffic behavior</i>.
	 *
	 * <p>
	 * Those agents will spawn on a random source node, pick a random sink
	 * node from there, and cross the city until their destination, and
	 * then perform a new cycle.
	 * </p>
	 *
	 * <p>
	 * The number of agents generated is determined by the
	 * <code>TrafficAgentNumber</code> parameter of the configuration.
	 * </p>
	 *
	 * @param context current context
	 * @return collection of <i>random traffic agents</i>
	 */
	@Override
	public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
		/* Can be performed now because nodes have been added to context,
		 * and arcs have been instantiated so that nodes have their incoming
		 * and outgoing arcs set up. The simulation graph is also built.
		 */
		generateSourceAndSinkNodes((OsmContext) context);
		
		Collection<Agent<?>> agents = new ArrayList<>();
		int agentCount = Integer.valueOf((String) context.getConfig().get("TrafficAgentNumber"));
		for(int i = 0; i < agentCount; i++){
			CarMover mover = new CarMover(5.0, -3.0, 10, 7.5);
			OsmAgentBody body = new OsmAgentBody(mover);
			agents.add(OsmAgent.randomTrafficOsmAgent(String.valueOf(i), (OsmContext) context, body));
		}
		return agents;
	}
	
	/**
	 * Generate source and sink nodes for the current graph in the given
	 * context, and adds them to this context.
	 *
	 * <p>
	 * The following rules are used:
	 * </p>
	 * <ul>
	 * 	<li> A node with no outgoing arc and at least one incoming
	 * 	arc is considered as a sink node.</li>
	 * 	<li> A node with no incoming arc and at least one outgoing arc
	 * 	is considered as a source node.</li>
	 * 	<li> A node with exactly one incoming arc and one outgoing arc
	 * 	that link the same two nodes is considered as a sink/source
	 * 	node. This situation actually represent dead ends that are not
	 * 	oneway, and normal city way outs (as roads are cut building the
	 * 	area of the simulation).</li>
	 * </ul>
	 *
	 * <p>
	 * Finally, attempts are made to build paths from each source node to
	 * every sink nodes to determine <i>destinations</i> and <i>sources</i>
	 * of each node.
	 * </p>
	 *
	 * <p>
	 * This function is automatically called in this scenario to build
	 * agents, but can be used in any scenario that wants to include random
	 * traffic agents.
	 * </p>
	 *
	 * @param context current context
	 */
	public static void generateSourceAndSinkNodes(OsmContext context) {
		SmartGov.logger.info("Generating source and sink nodes");
		for(Node node : context.nodes.values()) {
			if(node.getIncomingArcs().size() > 0 && node.getOutgoingArcs().size() == 0) {
				OsmSinkNode sinkNode = new OsmSinkNode(node.getId());
				context.getSinkNodes().put(sinkNode.getNodeId(), sinkNode);
			}
			else if (node.getIncomingArcs().size() == 0 && node.getOutgoingArcs().size() > 0) {
				OsmSourceNode sourceNode = new OsmSourceNode(node.getId());
				context.getSourceNodes().put(sourceNode.getNodeId(), sourceNode);
			}
			else if (node.getIncomingArcs().size() == 1 && node.getOutgoingArcs().size() == 1) {
				if(node.getIncomingArcs().get(0).getStartNode().getId() == node.getOutgoingArcs().get(0).getTargetNode().getId()) {
					OsmSinkSourceNode sinkSourceNode = new OsmSinkSourceNode(node.getId());
					context.getSinkNodes().put(sinkSourceNode.getNodeId(), sinkSourceNode);
					context.getSourceNodes().put(sinkSourceNode.getNodeId(), sinkSourceNode);
				}
			}
		}
		
		SmartGov.logger.info("Number of source nodes : " + context.getSourceNodes().size());
		SmartGov.logger.info("Number of sink nodes : " + context.getSinkNodes().size());
		
//		SmartGov.logger.info("Computing sources and destinations...");
//		Collection<String> sourceNodeIdsToRemove = new ArrayList<>();
//		Collection<String> sinkNodeIdsToRemove = new ArrayList<>();
		for (SourceNode sourceNode : context.getSourceNodes().values()) {
			for (SinkNode sinkNode : context.getSinkNodes().values()) {
				if (sourceNode.getNodeId() != sinkNode.getNodeId()) {
//					try {
//						context.getGraph().shortestPath(context.nodes.get(sourceNode.getNodeId()), context.nodes.get(sinkNode.getNodeId()));
//						
//						// If no error is thrown, a path is available between the two nodes
//						sourceNode.destinations().add(sinkNode);
//						sinkNode.sources().add(sourceNode);
//						
//					}
//					catch (IllegalArgumentException e) {
//						// No path available between the two nodes
//					}
					sourceNode.destinations().add(sinkNode);
					sinkNode.sources().add(sourceNode);
				}
			}
//			if (sourceNode.destinations().size() == 0) {
//				sourceNodeIdsToRemove.add(sourceNode.getNodeId());
//			}
//			for (SinkNode sinkNode : context.getSinkNodes().values()) {
//				if (sinkNode.sources().size() == 0) {
//					sinkNodeIdsToRemove.add(sinkNode.getNodeId());
//				}
//			}
		}
		
//		for(String id : sourceNodeIdsToRemove) {
//			context.getSourceNodes().remove(id);
//		}
//		for(String id : sinkNodeIdsToRemove) {
//			context.getSinkNodes().remove(id);
//		}
		
//		SmartGov.logger.info("All done.");
//		SmartGov.logger.info("Remaining source nodes : " + context.getSourceNodes().size());
//		SmartGov.logger.info("Remaining sink nodes : " + context.getSinkNodes().size());
	}

}
