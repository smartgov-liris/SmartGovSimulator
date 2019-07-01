package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.ArrayList;
import java.util.Collection;


public class OsmSinkSourceNode implements SinkNode, SourceNode {
	
	private String nodeId;
	private Collection<SourceNode> sources;
	private Collection<SinkNode> destinations;
	
	public OsmSinkSourceNode(String nodeId) {
		sources = new ArrayList<>();
		destinations = new ArrayList<>();
		this.nodeId = nodeId;
	}
	
	@Override
	public Collection<SinkNode> destinations() {
		return destinations;
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
