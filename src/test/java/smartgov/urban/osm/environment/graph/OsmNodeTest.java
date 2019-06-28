package smartgov.urban.osm.environment.graph;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import smartgov.urban.osm.utils.OsmLoader;

public class OsmNodeTest {

	public static Map<String, OsmNode> loadNodes() throws JsonParseException, JsonMappingException, IOException {
		OsmLoader<OsmNode> loader = new OsmLoader<>();
		List<OsmNode> osmNodes = loader.loadOsmElements(
				new File(OsmNodeTest.class.getResource("../../nodes.json").getFile()),
				OsmNode.class
				);
		
		Map<String, OsmNode> nodeMap = new HashMap<>();
		for(OsmNode node : osmNodes) {
			nodeMap.put(node.getId(), node);
		}
		
		return nodeMap;
	}
	@Test
	public void loadOsmNodesFromJson() throws JsonParseException, JsonMappingException, IOException {
		Collection<OsmNode> osmNodes = loadNodes().values();
		
		assertThat(
				osmNodes,
				hasSize(6421)
				);
		
		for (OsmNode osmNode : osmNodes) {
			assertThat(
					osmNode.getId(),
					notNullValue()
					);
			assertThat(
					osmNode.getPosition().x,
					notNullValue()
					);
			assertThat(
					osmNode.getPosition().y,
					notNullValue()
					);
		}
	}
}
