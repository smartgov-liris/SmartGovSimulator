package smartgov.urban.osm.scenario;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.scenario.lowLayer.ScenarioVisualization;

public class VisualizationScenarioTest {

	@Test
	public void testLoadOsmFeatures() {
		SmartGov smartGov = new SmartGov(
				new OsmContext(
						this.getClass().getResource("visualization_config.properties").getFile()
						)
				);
		
		assertThat(
				smartGov.getContext().getScenario() instanceof ScenarioVisualization,
				equalTo(true)
				);
		
		assertThat(
				smartGov.getContext().nodes.values(),
				hasSize(1179)
				);
		
		assertThat(
				((OsmContext) smartGov.getContext()).roads,
				hasSize(110)
				);
		
		assertThat(
				((OsmContext) smartGov.getContext()).arcs.values(),
				hasSize(2400)
				);
	}
}
