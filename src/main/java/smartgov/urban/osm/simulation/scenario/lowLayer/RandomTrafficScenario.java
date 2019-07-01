package smartgov.urban.osm.simulation.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.plan.Plan;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.environment.graph.events.AgentDestination;
import smartgov.core.events.EventHandler;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkSourceNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSourceNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SourceNode;
import smartgov.urban.osm.simulation.scenario.BasicOsmScenario;

public class RandomTrafficScenario extends BasicOsmScenario {
	
	public static final String name = "randomTraffic";
	
	@Override
	public Collection<? extends Arc> buildArcs(SmartGovContext context) {
		Collection<? extends Arc> arcs = super.buildArcs(context);
		
		return arcs;
	}

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
			agents.add(OsmAgent.randomTrafficOsmAgent(String.valueOf(i), (OsmContext) context));
		}
		return agents;
	}
	
	public static void generateSourceAndSinkNodes(OsmContext context) {
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
//		for(Node sourceNode : context.getSourceNodes().values()) {
//			context.agentsStock.put(sourceNode.getId(), new LinkedList<>());
//		}
		
//		for(Node sinkNode : context.getSinkNodes().values()) {
//			sinkNode.addAgentDestinationListener(new EventHandler<AgentDestination>() {
//				@Override
//				public void handle(AgentDestination event) {					
//					((OsmAgent) event.getAgent()).initialize();
//				}
//				
//			});
//		}
		Collection<String> sourceNodeIdsToRemove = new ArrayList<>();
		Collection<String> sinkNodeIdsToRemove = new ArrayList<>();
		for (SourceNode sourceNode : context.getSourceNodes().values()) {
			for (SinkNode sinkNode : context.getSinkNodes().values()) {
				if (sourceNode.getNodeId() != sinkNode.getNodeId()) {
					try {
						context.getGraph().shortestPath(context.nodes.get(sourceNode.getNodeId()), context.nodes.get(sinkNode.getNodeId()));
						
						// If no error is thrown, a path is available between the two nodes
						sourceNode.destinations().add(sinkNode);
						sinkNode.sources().add(sourceNode);
						
					}
					catch (IllegalArgumentException e) {
						// No path available between the two nodes
					}
				}
			}
			if (sourceNode.destinations().size() == 0) {
				sourceNodeIdsToRemove.add(sourceNode.getNodeId());
			}
			for (SinkNode sinkNode : context.getSinkNodes().values()) {
				if (sinkNode.sources().size() == 0) {
					sinkNodeIdsToRemove.add(sinkNode.getNodeId());
				}
			}
		}
		
		for(String id : sourceNodeIdsToRemove) {
			context.getSourceNodes().remove(id);
		}
		for(String id : sinkNodeIdsToRemove) {
			context.getSinkNodes().remove(id);
		}
		
		SmartGov.logger.info("Number of source nodes : " + context.getSourceNodes().size());
		SmartGov.logger.info("Number of sink nodes : " + context.getSinkNodes().size());
	}

}
