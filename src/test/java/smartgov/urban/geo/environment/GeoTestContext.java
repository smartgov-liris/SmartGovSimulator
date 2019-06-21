package smartgov.urban.geo.environment;

import smartgov.core.environment.TestContext;
import smartgov.core.simulation.Scenario;
import smartgov.urban.geo.simulation.GeoTestScenario;

public class GeoTestContext extends TestContext {

	public GeoTestContext(String configFile) {
		super(configFile);
	}
	
	@Override
	public Scenario loadScenario(String name) {
		switch(name) {
		case GeoTestScenario.name:
			return new GeoTestScenario();
		default:
			return null;
		}
	}
}
