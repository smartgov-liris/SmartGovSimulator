package smartgov.core.environment;

import smartgov.core.simulation.Scenario;
import smartgov.core.simulation.TestScenario;

public class TestContext extends SmartGovContext {

	public TestContext(String configFile) {
		super(configFile);
	}
	
	@Override
	public Scenario loadScenario(String name) {
		switch(name) {
		case TestScenario.name:
			return new TestScenario();
		default:
			return null;
		}
	}
}
