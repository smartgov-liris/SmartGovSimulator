package org.liris.smartgov.simulator.core.scenario;

import java.util.Collection;

import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;

/**
 * A simulation scenario.
 *
 * <p>
 * Scenarios define how Nodes, Arcs and Agents are built.
 * </p>
 */
public abstract class Scenario {

	/**
	 * Builds and adds Nodes, Arcs and Agents to the specified context.
	 *
	 * <p>
	 * Build functions are called is this ordered :
	 * <ol>
	 * 	<li> {@link #buildNodes buildNodes} </li>
	 * 	<li> {@link #buildArcs buildArcs} </li>
	 * 	<li> {@link #buildAgents buildAgents} </li>
	 * </ol>
	 * Items are added to the context at each call, so that
	 * <code>buildArcs</code> can use previously built Nodes
	 * from the context, and <code>buildAgents</code> can use built
	 * Nodes and Arcs.
	 *
	 * @param context current context, to which elements will be added
	 */
	public void loadWorld(SmartGovContext context) {
		// Build and add nodes
		for (Node node : buildNodes(context)) {
			context.nodes.put(node.getId(), node);
		}
		SmartGov.logger.info(context.nodes.size() + " nodes added to SmartGovContext");

		// Build and add arcs
		for (Arc arc : buildArcs(context)) {
			context.arcs.put(arc.getId(), arc);
		}
		SmartGov.logger.info(context.arcs.size() + " arcs added to SmartGovContext");
		
		/*
		 * Build the graph from the previously built nodes and arcs.
		 */
		context.buildGraph();
		
		// Build and add agents
		for (Agent<?> agent : buildAgents(context)) {
			context.agents.put(agent.getId(), agent);
		}
		SmartGov.logger.info(context.agents.size() + " agents added to SmartGovContext");
	}
	
	/**
	 * Automatically called to build nodes for this scenario.
	 *
	 * @param context current context, that can optionally be used to
	 * retrieve things.
	 *
	 * @return built nodes
	 */
	public abstract Collection<? extends Node> buildNodes(SmartGovContext context);

	/**
	 * Automatically called to build arcs for this scenario.
	 *
	 * <p>
	 * Called after <code>buildNodes</code>, so that Nodes in the argument
	 * context can be used.
	 * </p>
	 *
	 * @param context current context
	 *
	 * @return built arcs.
	 */
	public abstract Collection<? extends Arc> buildArcs(SmartGovContext context);

	/**
	 * Automatically called to build agents for this scenario.
	 *
	 * <p>
	 * Called after <code>buildNodes</code> and <code>buildArcs</code>,
	 * so that Nodes and Arcs in the argument context can be used.
	 * </p>
	 *
	 * @param context current context
	 *
	 * @return built agents.
	 */
	public abstract Collection<? extends Agent<?>> buildAgents(SmartGovContext context);
}
