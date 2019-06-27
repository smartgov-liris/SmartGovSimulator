package smartgov.urban.osm.environment.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import smartgov.urban.osm.utils.OsmLoader;

public class OsmRoadTest {

	@Test
	public void loadRoadsFromJson() throws JsonParseException, JsonMappingException, IOException {
		// Assert that we can load subclasses of osmWay
		OsmLoader<Road> loader = new OsmLoader<>();
		List<Road> roads = loader.loadOsmElements(
				new File(this.getClass().getResource("../../ways.json").getFile()),
				Road.class
				);
		
		assertThat(
				roads,
				hasSize(326)
				);
		
		for (Road road : roads) {
			assertThat(
					road instanceof Road,
					equalTo(true)
					);
			
			assertThat(
					road.getId(),
					notNullValue()
					);
			
			assertThat(
					road.getNodes(),
					hasSize(greaterThan(1))
					);
			
			assertThat(
					road.getAgentsOnRoad(),
					notNullValue()
					);
		}
	}
}
