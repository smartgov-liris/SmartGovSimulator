package smartgov.urban.osm.environment.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import smartgov.core.agent.moving.plan.Plan;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
import smartgov.urban.osm.utils.OsmLoader;

public class OsmRoadTest {
	
	public static final String complete_ways = "../../ways.json";
	private static final String very_simple_way = "ways.json";
	
	public static Map<String, Road> loadRoads(String file) throws JsonParseException, JsonMappingException, IOException {
		OsmLoader<Road> loader = new OsmLoader<>();
		List<Road> roads = loader.loadOsmElements(
				new File(OsmRoadTest.class.getResource(file).getFile()),
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
		Map<String, Road> roads = loadRoads(complete_ways);
		
		assertThat(
				roads.values(),
				hasSize(110)
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
		}
	}
	
	@Test
	public void testOnewayRoadsAreLoadedProperly() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Road> roads = loadRoads(complete_ways);
		
		int onewayRoadsCount = 0;
		for(Road road : roads.values()) {
			if (road.isOneway()) {
				onewayRoadsCount += 1;
			}
		}
		
		assertThat(
				onewayRoadsCount,
				equalTo(6)
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
	
	@Test
	public void testAddForwardAgentsToRoad() throws JsonParseException, JsonMappingException, IOException {
		OsmLoader<OsmNode> loader = new OsmLoader<>();
		List<OsmNode> nodes = loader.loadOsmElements(new File(OsmRoadTest.class.getResource("nodes.json").getFile()), OsmNode.class);
		
		// Nodes in this order : 1 -> 2 -> 3 -> 4 -> 1
		addAgentsToRoadTestSuite(RoadDirection.FORWARD, nodes);
	}

	@Test
	public void testAddBackwardAgentsToRoad() throws JsonParseException, JsonMappingException, IOException {
		OsmLoader<OsmNode> loader = new OsmLoader<>();
		List<OsmNode> nodes = loader.loadOsmElements(new File(OsmRoadTest.class.getResource("nodes.json").getFile()), OsmNode.class);
		
		List<OsmNode> orderedNodes = new ArrayList<>();
		for(int i = nodes.size() - 1; i >= 0; i--) {
			orderedNodes.add(nodes.get(i));
		}
		addAgentsToRoadTestSuite(RoadDirection.BACKWARD, orderedNodes);
	}
	
	
	private void addAgentsToRoadTestSuite(RoadDirection direction, List<OsmNode> orderedNodes) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Road> roads = loadRoads(very_simple_way);
		
		OsmArc fakeArc = mock(OsmArc.class);
		when(fakeArc.getRoadDirection()).thenReturn(direction);
		
		List<OsmAgentBody> fakeAgents = new ArrayList<>();
		for(int i = 0; i < 3 ; i++) {
			OsmNode origin = orderedNodes.get(i);
			
			Plan fakePlan = mock(Plan.class);
			when(fakePlan.getCurrentArc()).thenReturn(fakeArc);
			when(fakePlan.getCurrentNode()).thenReturn(origin);
			
			OsmAgentBody fakeAgent = mock(OsmAgentBody.class);
			when(fakeAgent.getPlan()).thenReturn(fakePlan);
			
			fakeAgents.add(fakeAgent);
		}
		Road simpleRoad = roads.get("1");
		
		// Step 1 : Add the second agent (spawn on node 2)
		simpleRoad.addAgent(fakeAgents.get(1));
		
		assertThat(
				simpleRoad.getAgentsOnRoad(direction),
				contains(fakeAgents.get(1))
				);
		
		// Step 2 : Add the first agent, that should be on node 1, after the agent on node 2
		simpleRoad.addAgent(fakeAgents.get(0));
		
		assertThat(
				simpleRoad.getAgentsOnRoad(direction),
				contains(fakeAgents.get(1), fakeAgents.get(0))
				);
		
		assertThat(
				simpleRoad.leaderOfAgent(fakeAgents.get(0)),
				equalTo(fakeAgents.get(1))
				);
		
		// Step 3 : Add the third agent, that should be on node 3, before the second agent
		simpleRoad.addAgent(fakeAgents.get(2));
		
		assertThat(
				simpleRoad.getAgentsOnRoad(direction),
				contains(fakeAgents.get(2), fakeAgents.get(1), fakeAgents.get(0))
				);
		
		assertThat(
				simpleRoad.leaderOfAgent(fakeAgents.get(0)),
				equalTo(fakeAgents.get(1))
				);
		
		assertThat(
				simpleRoad.leaderOfAgent(fakeAgents.get(1)),
				equalTo(fakeAgents.get(2))
				);
		
		assertThat(
				simpleRoad.leaderOfAgent(fakeAgents.get(2)),
				nullValue()
				);

		// Last situation : we want to make an agent spawn on node 2.
		// It should be added right after the second agent (that is on the same node).
		OsmNode origin = orderedNodes.get(1);
		
		Plan fakePlan = mock(Plan.class);
		when(fakePlan.getCurrentArc()).thenReturn(fakeArc);
		when(fakePlan.getCurrentNode()).thenReturn(origin);
		
		OsmAgentBody fakeAgent = mock(OsmAgentBody.class);
		when(fakeAgent.getPlan()).thenReturn(fakePlan);
		
		simpleRoad.addAgent(fakeAgent);
		
		assertThat(
				simpleRoad.getAgentsOnRoad(direction),
				contains(fakeAgents.get(2), fakeAgents.get(1), fakeAgent, fakeAgents.get(0))
				);
		
		assertThat(
				simpleRoad.leaderOfAgent(fakeAgent),
				equalTo(fakeAgents.get(1))
				);
		
		assertThat(
				simpleRoad.leaderOfAgent(fakeAgents.get(0)),
				equalTo(fakeAgent)
				);
	}
}
