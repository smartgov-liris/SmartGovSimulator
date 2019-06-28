package smartgov.urban.osm.environment.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import smartgov.urban.osm.utils.OsmLoader;

public class OsmRoadTest {
	
	public static Map<String, Road> loadRoads() throws JsonParseException, JsonMappingException, IOException {
		OsmLoader<Road> loader = new OsmLoader<>();
		List<Road> roads = loader.loadOsmElements(
				new File(OsmRoadTest.class.getResource("../../ways.json").getFile()),
				Road.class
				);
		
		Map<String, Road> roadMap = new HashMap<>();
		for(Road road : roads) {
			roadMap.put(road.getId(), road);
		}
		
		return roadMap;
	}

	@Test
	public void loadRoadsFromJson() throws JsonParseException, JsonMappingException, IOException {
		// Assert that we can load subclasses of osmWay
		Map<String, Road> roads = loadRoads();
		
		assertThat(
				roads.values(),
				hasSize(326)
				);
		
		for (Road road : roads.values()) {
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
					road.getForwardAgentsOnRoad(),
					notNullValue()
					);
			
			assertThat(
					road.getForwardAgentsOnRoad(),
					notNullValue()
					);
		}
	}
	
	@Test
	public void testOnewayRoadsAreLoadedProperly() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Road> roads = loadRoads();
		
		int onewayRoadsCount = 0;
		for(Road road : roads.values()) {
			if (road.isOneway()) {
				onewayRoadsCount += 1;
			}
		}
		
		assertThat(
				onewayRoadsCount,
				equalTo(7)
				);
		
		Road reversedOneWayRoad = roads.get("391464268");
		
		assertThat(
				reversedOneWayRoad.getId(),
				equalTo("391464268")
				);
		
		assertThat(
				reversedOneWayRoad.getNodes(),
				contains("3946790933", "213922180")
				);
		
	}
}
