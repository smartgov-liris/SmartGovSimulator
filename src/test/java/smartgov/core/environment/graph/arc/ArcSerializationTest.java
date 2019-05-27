package smartgov.core.environment.graph.arc;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import smartgov.core.environment.graph.node.Node;

public class ArcSerializationTest {

	@Test
	public void serializeArc() throws JsonProcessingException {
		Node<?> startNode = new Node<>("1");
		Node<?> targetNode = new Node<>("2");
		Arc<?> arc = new Arc<>("1", startNode, targetNode, 2.36);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(arc);
		
		assertThat(
				json,
				equalTo("{\"id\":\"1\",\"startNode\":\"1\",\"targetNode\":\"2\",\"length\":2.36}")
				);
	}
}