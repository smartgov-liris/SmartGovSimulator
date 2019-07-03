package smartgov.urban.osm.simulation.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;

import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.urban.osm.simulation.scenario.BasicOsmScenario;

/**
 * Specific Scenario with the minimum functions to start visualization of
 * osm features without agents.
 *
 */
public class ScenarioVisualization extends BasicOsmScenario {
	
	public static final String name = "Visualization";
	
	public ScenarioVisualization() {
		super();
	}

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
