package org.liris.smartgov.simulator.core.environment;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class TestSmartGovContext {
	
	public static TestContext loadTestContext() {
		return new TestContext(TestSmartGovContext.class.getResource("test_config.properties").getFile());
	}
	
	@Test
	public void loadContext() {
		TestContext context = loadTestContext();
		
		assertThat(
				(String) context.getConfig().get("scenario"),
				equalTo("TestScenario")
				);
	}
	
}
