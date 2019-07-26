package smartgov.urban.osm.utils;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import smartgov.core.environment.graph.Node;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.OsmNodeTest;
import smartgov.urban.osm.environment.graph.OsmRoadTest;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.environment.graph.factory.DefaultOsmArcFactory;

public class ArcsBuilderTest {
	
	public static Map<String, OsmArc> buildArcs() throws JsonParseException, JsonMappingException, IOException {
		Collection<Road> roads = OsmRoadTest.loadRoads(OsmRoadTest.complete_ways).values();
		Map<String, OsmNode> nodes = OsmNodeTest.loadNodes(OsmNodeTest.testNodes);
		
		List<OsmArc> arcs = OsmArcsBuilder.buildArcs(nodes, roads, new DefaultOsmArcFactory());
		
		Map<String, OsmArc> arcMap = new HashMap<>();
		
		for (OsmArc arc : arcs) {
			arcMap.put(arc.getId(), arc);
		}
		
		return arcMap;
	}
	@Test
	public void arcsAreCreatedPropertly () throws JsonParseException, JsonMappingException, IOException {
		Collection<OsmArc> arcs = buildArcs().values();
		Collection<Road> roads = OsmRoadTest.loadRoads(OsmRoadTest.complete_ways).values();
		
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
	
	@Test
	public void testFixDeadEnds() throws JsonParseException, JsonMappingException, IOException {
		Collection<Road> roads = OsmRoadTest.loadRoads(
				new File(ArcsBuilderTest.class.getResource("ways_dead_end.json").getFile())
				).values();
		Map<String, OsmNode> nodes = OsmNodeTest.loadNodes(new File(ArcsBuilderTest.class.getResource("nodes_dead_end.json").getFile()));
		
		List<OsmArc> arcs = OsmArcsBuilder.buildArcs(nodes, roads, new DefaultOsmArcFactory());
		
		Map<String, OsmArc> arcMap = new HashMap<>();
		
		for (OsmArc arc : arcs) {
			arcMap.put(arc.getId(), arc);
		}
		
		OsmContext context = mock(OsmContext.class);
		context.arcs = new TreeMap<>(arcMap);
		context.nodes = new TreeMap<>(nodes);
		
		assertThat(
				context.nodes.get("354040664").getOutgoingArcs(),
				hasSize(0)
				);
		
		assertThat(
				((OsmNode) context.nodes.get("354040664")).getRoad().isOneway(),
				equalTo(true)
				);
		
		assertThat(
				context.arcs.values(),
				hasSize(6)
				);
		
		
		OsmArcsBuilder.fixDeadEnds(context, new DefaultOsmArcFactory());
		
		assertThat(
				context.nodes.get("354040664").getOutgoingArcs(),
				hasSize(1)
				);
		
		assertThat(
				context.arcs.values(),
				hasSize(8)
				);
		
		Boolean noDeadEnd = true;
		for(Node node : context.nodes.values()) {
			if(node.getOutgoingArcs().isEmpty() || node.getIncomingArcs().isEmpty()) {
				noDeadEnd = false;
			}
		}
		
		assertThat(
				((OsmNode) context.nodes.get("354040664")).getRoad().isOneway(),
				equalTo(false)
				);
		
		assertThat(
				noDeadEnd,
				equalTo(true)
				);
		
	}
	
	@Test
	public void testFixDeadEnds2() throws JsonParseException, JsonMappingException, IOException {
		Collection<Road> roads = OsmRoadTest.loadRoads(
				new File(ArcsBuilderTest.class.getResource("ways_dead_end_2.json").getFile())
				).values();
		Map<String, OsmNode> nodes = OsmNodeTest.loadNodes(new File(ArcsBuilderTest.class.getResource("nodes_dead_end_2.json").getFile()));
		
		List<OsmArc> arcs = OsmArcsBuilder.buildArcs(nodes, roads, new DefaultOsmArcFactory());
		
		Map<String, OsmArc> arcMap = new HashMap<>();
		
		for (OsmArc arc : arcs) {
			arcMap.put(arc.getId(), arc);
		}
		
		OsmContext context = mock(OsmContext.class);
		context.arcs = new TreeMap<>(arcMap);
		context.nodes = new TreeMap<>(nodes);
		
		assertThat(
				context.nodes.get("354040664").getIncomingArcs(),
				hasSize(0)
				);
		
		assertThat(
				((OsmNode) context.nodes.get("354040664")).getRoad().isOneway(),
				equalTo(true)
				);
		
		assertThat(
				context.arcs.values(),
				hasSize(6)
				);
		
		
		OsmArcsBuilder.fixDeadEnds(context, new DefaultOsmArcFactory());
		
		assertThat(
				context.nodes.get("354040664").getIncomingArcs(),
				hasSize(1)
				);
		
		assertThat(
				context.arcs.values(),
				hasSize(8)
				);
		
		Boolean noDeadEnd = true;
		for(Node node : context.nodes.values()) {
			if(node.getOutgoingArcs().isEmpty() || node.getIncomingArcs().isEmpty()) {
				noDeadEnd = false;
			}
		}
		
		assertThat(
				((OsmNode) context.nodes.get("354040664")).getRoad().isOneway(),
				equalTo(false)
				);
		
		assertThat(
				noDeadEnd,
				equalTo(true)
				);
		
	}
	
	/*
	 * This is an unsupported case.
	 * 
	 * 1 <=> 2 => 3 => 4 <=> 5
	 * 
	 * In that case, the graph is not fully connected and if an agent
	 * go to 5 or 4, it will be stuck.
	 */
	// @Test
	public void testFixDeadEnds3() throws JsonParseException, JsonMappingException, IOException {
		Collection<Road> roads = OsmRoadTest.loadRoads(
				new File(ArcsBuilderTest.class.getResource("ways_dead_end_2.json").getFile())
				).values();
		Map<String, OsmNode> nodes = OsmNodeTest.loadNodes(new File(ArcsBuilderTest.class.getResource("nodes_dead_end_2.json").getFile()));
		
		List<OsmArc> arcs = OsmArcsBuilder.buildArcs(nodes, roads, new DefaultOsmArcFactory());
		
		Map<String, OsmArc> arcMap = new HashMap<>();
		
		for (OsmArc arc : arcs) {
			arcMap.put(arc.getId(), arc);
		}
		
		OsmContext context = mock(OsmContext.class);
		context.arcs = new TreeMap<>(arcMap);
		context.nodes = new TreeMap<>(nodes);
		
		assertThat(
				context.nodes.get("4").getOutgoingArcs(),
				hasSize(1)
				);
		assertThat(
				context.arcs.values(),
				hasSize(6)
				);
	}

}
