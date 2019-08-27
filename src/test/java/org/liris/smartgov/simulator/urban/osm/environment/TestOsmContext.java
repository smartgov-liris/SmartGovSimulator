package org.liris.smartgov.simulator.urban.osm.environment;

import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.urban.osm.agent.mover.CarMoverTestScenario;
import org.liris.smartgov.simulator.urban.osm.environment.OsmContext;

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
