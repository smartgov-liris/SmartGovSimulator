package smartgov.core.environment;

import smartgov.core.simulation.Scenario;
import smartgov.core.simulation.TestScenario;
import smartgov.urban.geo.simulation.GeoTestScenario;

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
