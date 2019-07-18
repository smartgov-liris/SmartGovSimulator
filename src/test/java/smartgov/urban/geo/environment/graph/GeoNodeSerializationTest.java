package smartgov.urban.geo.environment.graph;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import smartgov.urban.geo.utils.LatLon;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

public class GeoNodeSerializationTest {

	@Test
	public void serializeGeoNode() throws JsonProcessingException {
		GeoNode node = new GeoNode("1", new LatLon(1.87, 10.12));
		GeoNode fakeNode = new GeoNode("2", new LatLon(0.8, 1));
		GeoArc arc1 = new GeoArc("1", node, fakeNode);
		GeoArc arc2 = new GeoArc("2", node, fakeNode);
		GeoArc arc3 = new GeoArc("3", fakeNode, node);
		node.setIncomingArcs(Arrays.asList(arc3));
		node.setOutgoingArcs(Arrays.asList(arc1, arc2));
		
		String json = new ObjectMapper().writeValueAsString(node);
		
		assertThat(
				json,
				equalTo("{\"id\":\"1\",\"outgoingArcs\":[\"1\",\"2\"],\"incomingArcs\":[\"3\"],\"position\":[1.87,10.12]}")
				);
	}
}