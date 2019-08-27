package org.liris.smartgov.simulator.urban.osm.scenario;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.urban.osm.environment.OsmContext;
import org.liris.smartgov.simulator.urban.osm.scenario.lowLayer.ScenarioVisualization;

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
				((OsmContext) smartGov.getContext()).roads.values(),
				hasSize(110)
				);
		
		assertThat(
				((OsmContext) smartGov.getContext()).arcs.values(),
				hasSize(2400)
				);
	}
}
