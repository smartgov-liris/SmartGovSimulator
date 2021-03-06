package org.liris.smartgov.simulator.urban.osm.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;

import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmNode;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;
import org.liris.smartgov.simulator.urban.osm.environment.graph.factory.OsmArcFactory;
import org.liris.smartgov.simulator.urban.osm.scenario.GenericOsmScenario;

/**
 * Specific Scenario with the minimum functions to start visualization of
 * osm features without agents.
 *
 */
public class ScenarioVisualization<Tnode extends OsmNode, Troad extends Road> extends GenericOsmScenario<Tnode, Troad> {
	
	public ScenarioVisualization(Class<Tnode> nodeClass, Class<Troad> roadClass, OsmArcFactory<?> osmArcFactory) {
		super(nodeClass, roadClass, osmArcFactory);
	}

	public static final String name = "Visualization";
	


	/**
	 * Does not build any agents, so that just the OSM graph will be
	 * loaded.
	 *
	 * @param context current context
	 * @return empty collection
	 */
	@Override
	public Collection<Agent<?>> buildAgents(SmartGovContext context) {
		return new ArrayList<>();
	}

}
