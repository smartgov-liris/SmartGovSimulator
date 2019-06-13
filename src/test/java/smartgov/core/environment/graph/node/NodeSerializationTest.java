package smartgov.core.environment.graph.node;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

public class NodeSerializationTest {

	@Test
	public void serializeSimpleNode() throws JsonProcessingException {
		Node node = new Node("1");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(node);

		assertThat(
				json,
				equalTo("{\"id\":\"1\",\"outgoingArcs\":[],\"incomingArcs\":[]}")
				);
	}
	
	@Test
	public void serializeNodeWithArcs() throws JsonProcessingException {
		Node node = new Node("1");
		Node fakeNode = new Node("2");
		Arc arc1 = new Arc("1", node, fakeNode, 1);
		Arc arc2 = new Arc("2", node, fakeNode, 2);
		Arc arc3 = new Arc("3", fakeNode, node, 1);
		node.setIncomingArcs(Arrays.asList(arc3));
		node.setOutgoingArcs(Arrays.asList(arc1, arc2));
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(node);
		
		assertThat(
				json,
				equalTo("{\"id\":\"1\",\"outgoingArcs\":[\"1\",\"2\"],\"incomingArcs\":[\"3\"]}")
				);
	}
}
