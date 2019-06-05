package smartgov.core.simulation;

import java.util.Collection;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.OrientedGraph;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;

public abstract class Scenario {

	/**
	 * Add elements to context and instantiates agents.
	 * @param context Repast Simphony context.
	 * @return Updated context used by SmartGov simulator
	 */
	public void loadWorld(SmartGovContext context) {
		for (Node node : buildNodes(context)) {
			context.nodes.put(node.getId(), node);
		}
		SmartGov.logger.info(context.nodes.size() + " nodes added to SmartGovContext");
		for (Arc arc : buildArcs(context)) {
			context.arcs.put(arc.getId(), arc);
		}
		SmartGov.logger.info(context.arcs.size() + " arcs added to SmartGovContext");
		
		createGraph(context);
		
		for (Agent agent : buildAgents(context)) {
			context.agents.put(agent.getId(), agent);
		}
		SmartGov.logger.info(context.agents.size() + " agents added to SmartGovContext");
	}
	
	private void createGraph(SmartGovContext context) {
		SmartGov.logger.info("Creating the simulation OrientedGraph");
		OrientedGraph orientedGraph = new OrientedGraph(context.nodes, context.arcs);
		context.graph = orientedGraph;
	}
	
	public abstract Collection<Node> buildNodes(SmartGovContext context);
	public abstract Collection<Arc> buildArcs(SmartGovContext context);
	public abstract Collection<Agent> buildAgents(SmartGovContext context);
}
