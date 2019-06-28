package smartgov.urban.osm.utils;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.OsmNodeTest;
import smartgov.urban.osm.environment.graph.OsmRoadTest;
import smartgov.urban.osm.environment.graph.Road;

public class ArcsBuilderTest {
	
	public static Map<String, OsmArc> buildArcs() throws JsonParseException, JsonMappingException, IOException {
		Collection<Road> roads = OsmRoadTest.loadRoads().values();
		Map<String, OsmNode> nodes = OsmNodeTest.loadNodes();
		
		List<OsmArc> arcs = OsmArcsBuilder.buildArcs(nodes, roads);
		
		Map<String, OsmArc> arcMap = new HashMap<>();
		
		for (OsmArc arc : arcs) {
			arcMap.put(arc.getId(), arc);
		}
		
		return arcMap;
	}
	@Test
	public void arcsAreCreatedPropertly () throws JsonParseException, JsonMappingException, IOException {
		Collection<OsmArc> arcs = buildArcs().values();
		Collection<Road> roads = OsmRoadTest.loadRoads().values();
		
		// There whould be at least one arc for each road
		assertThat(
				arcs.size() >= roads.size(),
				equalTo(true)
				);
		
		Map<String, Map<RoadDirection, List<OsmArc>>> roadArcsMap = new HashMap<>();
		for (Road road : roads) {
			roadArcsMap.put(road.getId(), new HashMap<>());
			roadArcsMap.get(road.getId()).put(RoadDirection.FORWARD, new ArrayList<>());
			roadArcsMap.get(road.getId()).put(RoadDirection.BACKWARD, new ArrayList<>());
		}
		
		for (OsmArc arc : arcs) {
			roadArcsMap
				.get(arc.getRoad().getId())
				.get(arc.getRoadDirection())
				.add(arc);
		}
		
		for(Road road : roads) {
			/*
			 * For FORWARD and BACKWARD direction, we will check that exactly one arc has been built for each
			 * two node succession of the original road's node refs.
			 */
			List<OsmArc> arcsToMatch = new ArrayList<>(roadArcsMap.get(road.getId()).get(RoadDirection.FORWARD));
			
			for (int i = 0; i < road.getNodes().size() - 1; i++) {
				OsmArc builtArc = null;
				int j = 0;
				while (j < arcsToMatch.size() && builtArc == null) {
					// Loop until the first matching arc is found, and until the end of the list (if no arc matches)
					if (
						arcsToMatch.get(j).getStartNode().getId().equals(road.getNodes().get(i))
						&& arcsToMatch.get(j).getTargetNode().getId().equals(road.getNodes().get(i + 1)))
					{
						builtArc = arcsToMatch.get(j);
					}
					j++;
				}
				// At least on matching arc has been found
				assertThat(
						builtArc,
						notNullValue()
						);
				assertThat(
					arcsToMatch.remove(builtArc),
					equalTo(true)
					);
			}
			
			// Exactly one arc matches for each nodes
			assertThat(
					arcsToMatch,
					hasSize(0)
					);
			
			if(road.isOneway()) {
				// No backward arc should be built
				assertThat(
					roadArcsMap.get(road.getId()).get(RoadDirection.BACKWARD),
					hasSize(0)
					);
			}
			
			// Same check in reverse order, if a backward direction exists
			else {
				arcsToMatch = new ArrayList<>(roadArcsMap.get(road.getId()).get(RoadDirection.BACKWARD));
				
				for (int i = road.getNodes().size() - 1; i > 0 ; i--) {
					OsmArc builtArc = null;
					int j = 0;
					while (j < arcsToMatch.size() && builtArc == null) {
						// Loop until the first matching arc is found, and until the end of the list (if no arc matches)
						if (
							arcsToMatch.get(j).getStartNode().getId().equals(road.getNodes().get(i))
							&& arcsToMatch.get(j).getTargetNode().getId().equals(road.getNodes().get(i - 1)))
						{
							builtArc = arcsToMatch.get(j);
						}
						j++;
					}
					// At least on matching arc has been found
					assertThat(
							builtArc,
							notNullValue()
							);
					assertThat(
						arcsToMatch.remove(builtArc),
						equalTo(true)
						);
				}
				
				// Exactly one arc matches for each nodes
				assertThat(
						arcsToMatch,
						hasSize(0)
						);
			}
		}
		
	}

}
