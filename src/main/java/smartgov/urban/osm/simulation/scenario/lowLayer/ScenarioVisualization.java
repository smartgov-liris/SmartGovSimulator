package smartgov.urban.osm.simulation.scenario.lowLayer;

import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.simulation.scenario.OsmScenario;

/**
 * Specific Scenario with the minimum functions to start visualization of
 * SmartGov environment without human agents.
 * <strike>Create an environment representation based on files in VisualizationTest
 * folder.</strike>
 * @author Simon
 *
 */
public class ScenarioVisualization extends OsmScenario {
	
	public static final String name = "Visualization";
	
	public ScenarioVisualization(OsmContext environment) {
		super(environment);
	}

	@Override
	protected void createAgents() {
		// No agents for this scenario
		
	}

}
