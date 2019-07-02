package smartgov.urban.osm.environment.graph;

import smartgov.core.simulation.Scenario;
import smartgov.urban.osm.agent.mover.CarMoverTestScenario;
import smartgov.urban.osm.environment.OsmContext;

public class TestOsmContext extends OsmContext {

	public TestOsmContext(String configFile) {
		super(configFile);
	}

	@Override
	public Scenario loadScenario(String name) {
		switch(name) {
		case CarMoverTestScenario.name:
			return new CarMoverTestScenario();
		default:
			return null;
		}
	}
	
	
}
