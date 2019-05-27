package smartgov;

import org.junit.Test;

import smartgov.core.environment.SmartGovContext;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.Properties;

public class SmartGovTest {
	
	private SmartGov loadSmartGov() {
		return new SmartGov(new SmartGovContext(this.getClass().getResource("test_config_file.properties").getFile()));
	}
	
    @Test
    public void testBuildApp() {
        assertThat(
        		loadSmartGov().getSimulationBuilder().getContext().getConfig(),
        		notNullValue()
        		);
        assertThat(
        		loadSmartGov().getSimulationBuilder().getContext().getScenario(),
        		nullValue()
        		);
    }
    
    @Test
    public void testConfigFile() {
    	Properties config = loadSmartGov().getSimulationBuilder().getContext().getConfig();
    	assertThat(
    			Integer.valueOf((String) config.get("AgentNumber")),
    			equalTo(10)
    			);
    }
}
