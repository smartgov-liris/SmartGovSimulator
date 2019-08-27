package org.liris.smartgov.simulator.core.environment;

import java.util.Map;
import java.util.TreeMap;

import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Graph;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Context implementation, that contains common elements to any simulation.
 * 
 * @author pbreugnot
 *
 */
public class SmartGovContext extends AbstractContext {

	public static final GeometryFactory GEOFACTORY = new GeometryFactory();
	
	/**
	 * Agents currently living in the simulation.
	 */
	public Map<String, Agent<?>> agents;

	/**
	 * Nodes currently used in the simulation.
	 */
	public Map<String, Node> nodes;

	/**
	 * Arcs currently used in the simulation.
	 */
	public Map<String, Arc> arcs;
	
	private Graph graph;
	
	/**
	 * Context constructor.
	 *
	 * @param configFile Absolute path of the configuration file to load.
	 */
	public SmartGovContext(String configFile) {
		super(configFile);
		agents = new TreeMap<>();
		nodes = new TreeMap<>();
		arcs = new TreeMap<>();
	}
	
	/**
	 * Current simulation graph.
	 *
	 * @return current graph
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * Builds the context graph from the current context arcs and nodes.
	 */
	public void buildGraph() {
		SmartGov.logger.info("Creating the simulation Graph");
		graph = new Graph(nodes, arcs);
	}
	
	@Override
	public void clear(){
		agents.clear(); //Agents are ordered by their id
		nodes.clear();
		arcs.clear();
	}

	@Override
	protected Scenario loadScenario(String scenarioName) {
		SmartGov.logger.info("Loading Scenario : " + scenarioName);
		return null;
	}
}
