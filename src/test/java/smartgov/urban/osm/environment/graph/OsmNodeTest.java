package smartgov.urban.osm.environment.graph;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import smartgov.urban.osm.utils.OsmLoader;

public class OsmNodeTest {

	@Test
	public void loadOsmNodesFromJson() throws JsonParseException, JsonMappingException, IOException {
		OsmLoader<OsmNode> loader = new OsmLoader<>();
		List<OsmNode> osmNodes = loader.loadOsmElements(
				new File(this.getClass().getResource("../../nodes.json").getFile()),
				OsmNode.class
				);
		
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
