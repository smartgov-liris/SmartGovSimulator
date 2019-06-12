package smartgov.core.agent.moving.events.scenario;

import smartgov.core.environment.TestContext;
import smartgov.core.simulation.Scenario;

public class TestEventsContext extends TestContext {

	public TestEventsContext() {
		super(TestEventsContext.class.getResource("test_events_config.properties").getFile());
	}

	@Override
	public Scenario loadScenario(String name) {
		switch(name) {
		case TestEventsScenario.name:
			return new TestEventsScenario();
		default:
			return null;
		}
	}
}
