package smartgov.models.lez.environment;

import smartgov.core.simulation.Scenario;
import smartgov.models.lez.simulation.scenario.PollutionScenario;
import smartgov.urban.osm.environment.OsmContext;

public class LezContext extends OsmContext {

	public LezContext(String configFile) {
		super(configFile);
	}

	@Override
	public Scenario loadScenario(String scenarioName) {
		Scenario superScenario = super.loadScenario(scenarioName);
		if (superScenario != null) {
			return superScenario;
		}
		switch(scenarioName){
			case PollutionScenario.name:
				return new PollutionScenario(this);
			default:
				return null;
		}
	}
}
