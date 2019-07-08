package smartgov.core.environment;

import smartgov.core.scenario.Scenario;
import smartgov.core.scenario.TestScenario;
import smartgov.urban.geo.scenario.GeoTestScenario;

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
