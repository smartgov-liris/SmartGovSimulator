package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.ArrayList;
import java.util.Collection;


/**
 * A node that can be used both as origin and destination for agents.
 */
public class OsmSinkSourceNode implements SinkNode, SourceNode {
	
	private String nodeId;
	private Collection<SourceNode> sources;
	private Collection<SinkNode> destinations;
	
	/**
	 * OsmSinkSourceNode constructor.
	 *
	 * @param nodeId is of the associated osm node
	 */
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
