package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.ArrayList;
import java.util.Collection;

public class OsmSourceNode implements SourceNode {
	
	private String nodeId;
	private Collection<SinkNode> destinations;

	public OsmSourceNode(String nodeId) {
		destinations = new ArrayList<>();
		this.nodeId = nodeId;
	}
	
	@Override
	public Collection<SinkNode> destinations() {
		return destinations;
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}
}
