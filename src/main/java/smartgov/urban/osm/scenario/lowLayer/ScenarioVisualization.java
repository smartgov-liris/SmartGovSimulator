package smartgov.urban.osm.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;

import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.environment.graph.factory.OsmArcFactory;
import smartgov.urban.osm.scenario.GenericOsmScenario;

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
