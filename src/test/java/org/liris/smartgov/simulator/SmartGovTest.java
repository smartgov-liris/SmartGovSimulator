package org.liris.smartgov.simulator;

import org.junit.Test;
import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.environment.TestSmartGovContext;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class SmartGovTest {
	
	public static SmartGov loadSmartGov() {
		return new SmartGov(TestSmartGovContext.loadTestContext());
	}
	
    @Test
    public void testBuildApp() {
        assertThat(
        		loadSmartGov().getSimulationBuilder().getContext().getConfig(),
        		notNullValue()
        		);
        assertThat(
        		SmartGov.getRuntime(),
        		notNullValue()
        		);
    }
}
