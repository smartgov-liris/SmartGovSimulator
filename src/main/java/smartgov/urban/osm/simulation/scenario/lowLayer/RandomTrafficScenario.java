package smartgov.urban.osm.simulation.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.Plan;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.events.EventHandler;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.simulation.scenario.BasicOsmScenario;

public class RandomTrafficScenario extends BasicOsmScenario {
	
	public static final String name = "randomTraffic";
	
	@Override
	public Collection<? extends Arc> buildArcs(SmartGovContext context) {
		Collection<? extends Arc> arcs = super.buildArcs(context);
		// Can be performed now because nodes have been added to context,
		// And arcs have been instantiated so that nodes have their incoming
		// and outgoing arcs set up.
				
		generateSourceAndSinkNodes((OsmContext) context);
		
		return arcs;
	}

	@Override
	public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
		Collection<Agent<?>> agents = new ArrayList<>();
		int agentCount = Integer.valueOf((String) context.getConfig().get("AgentNumber"));
		for(int i = 0; i < agentCount; i++){
			agents.add(OsmAgent.randomTrafficOsmAgent(String.valueOf(i), (OsmContext) context));
		}
		return agents;
	}
	
	private void generateSourceAndSinkNodes(OsmContext context) {
		for(Node node : context.nodes.values()) {
			if(node.getIncomingArcs().size() > 0 && node.getOutgoingArcs().size() == 0) {
				context.getSinkNodes().put(node.getId(), node);
			}
			else if (node.getIncomingArcs().size() == 0 && node.getOutgoingArcs().size() > 0) {
				context.getSourceNodes().put(node.getId(), node);
			}
			else if (node.getIncomingArcs().size() == 1 && node.getOutgoingArcs().size() == 1) {
				if(node.getIncomingArcs().get(0).getStartNode().getId() == node.getOutgoingArcs().get(0).getTargetNode().getId()) {
					context.getSinkNodes().put(node.getId(), node);
					context.getSourceNodes().put(node.getId(), node);
				}
			}
		}
		for(Node sourceNode : context.getSourceNodes().values()) {
			context.agentsStock.put(sourceNode.getId(), new LinkedList<>());
		}
		
		for(Node sinkNode : context.getSinkNodes().values()) {
			sinkNode.addAgentArrivalListener(new EventHandler<AgentArrival>() {
				@Override
				public void handle(AgentArrival event) {
					Plan plan = ((MovingAgentBody) event.getAgent().getBody()).getPlan();
					if(plan != null && plan.isPathComplete()) {
						// Agent reached the sink node, and this is its destination.					
						((OsmAgent) event.getAgent()).initialize();
					}
				}
				
			});
		}
		SmartGov.logger.info("Number of source nodes : " + context.getSourceNodes().size());
		SmartGov.logger.info("Number of sink nodes : " + context.getSinkNodes().size());
	}

}
