package smartgov.urban.osm.environment.graph;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmWay {

	private String id;
	
	// TODO : Must be unserialized with a custom deserializer.
	private List<String> nodeRefs;

	@JsonCreator
	public OsmWay(
		@JsonProperty("id") String id,
		@JsonProperty("nodeRefs") List<String> nodeRefs) {
		this.id = id;
		this.nodeRefs = nodeRefs;
	}

	public String getId() {
		return id;
	}
	
	public List<String> getNodes() {
		return nodeRefs;
	}
}
