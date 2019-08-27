package org.liris.smartgov.simulator.core.agent.moving.events.scenario;

import org.liris.smartgov.simulator.core.environment.TestContext;
import org.liris.smartgov.simulator.core.scenario.Scenario;

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
