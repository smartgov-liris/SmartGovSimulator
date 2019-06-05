package smartgov;

import org.junit.Test;

import smartgov.core.environment.TestSmartGovContext;

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
