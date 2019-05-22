package smartgov;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.Properties;

public class SmartGovTest {
	
	private SmartGov loadSmartGov() {
		return new SmartGov(this.getClass().getResource("test_config_file.properties").getFile());
	}
	
    @Test
    public void testBuildApp() {
        assertThat(
        		loadSmartGov().getSimulationBuilder().getContext().getConfig(),
        		notNullValue()
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
