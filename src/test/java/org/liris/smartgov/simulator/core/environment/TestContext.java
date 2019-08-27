package org.liris.smartgov.simulator.core.environment;

import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.core.scenario.TestScenario;
import org.liris.smartgov.simulator.urban.geo.scenario.GeoTestScenario;

public class TestContext extends SmartGovContext {

	public TestContext(String configFile) {
		super(configFile);
	}
	
	@Override
	public Scenario loadScenario(String name) {
		switch(name) {
		case TestScenario.name:
			return new TestScenario();
		case GeoTestScenario.name:
			return new GeoTestScenario();
		default:
			return null;
		}
	}
}
