package smartgov.core.main;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import smartgov.core.environment.TestSmartGovContext;
import smartgov.core.simulation.TestScenario;

public class TestSimulationBuilder {

	@Test
	public void testLoadWorld() {
		SimulationBuilder simulationBuilder = new SimulationBuilder(TestSmartGovContext.loadTestContext());
		
		assertThat(
				simulationBuilder.getContext().getScenario() instanceof TestScenario,
				equalTo(true)
				);
		
		assertThat(
			simulationBuilder.getContext().nodes.keySet(),
			hasSize(5)
			);
		
		assertThat(
			simulationBuilder.getContext().arcs.keySet(),
			hasSize(10)
			);
		
		assertThat(
			simulationBuilder.getContext().graph,
			notNullValue()
				);
	}
}
