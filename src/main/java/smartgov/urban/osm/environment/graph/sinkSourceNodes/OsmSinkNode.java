package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.ArrayList;
import java.util.Collection;


public class OsmSinkNode implements SinkNode {
	
	private String nodeId;
	private Collection<SourceNode> sources;

	public OsmSinkNode(String nodeId) {
		sources = new ArrayList<>();
		this.nodeId = nodeId;
	}
	
	@Override
	public Collection<SourceNode> sources() {
		return sources;
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}

}
