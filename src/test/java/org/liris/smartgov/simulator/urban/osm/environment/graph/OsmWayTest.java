package org.liris.smartgov.simulator.urban.osm.environment.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmWay;
import org.liris.smartgov.simulator.urban.osm.utils.OsmLoader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class OsmWayTest {
	
	@Test
	public void loadOsmWaysFromJson() throws JsonParseException, JsonMappingException, IOException {
		OsmLoader<OsmWay> loader = new OsmLoader<>();
		List<OsmWay> osmWays = loader.loadOsmElements(
				new File(this.getClass().getResource("../../ways.json").getFile()),
				OsmWay.class
				);
		
		assertThat(
				osmWays,
				hasSize(110)
				);
		
		for (OsmWay osmWay : osmWays) {
			assertThat(
					osmWay.getId(),
					notNullValue()
					);
			assertThat(
					osmWay.getNodes(),
					hasSize(greaterThan(1))
					);
		}
	}
}
