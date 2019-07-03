package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Osm implementation of a sink node.
 */
public class OsmSinkNode implements SinkNode {
	
	private String nodeId;
	private Collection<SourceNode> sources;

	/**
	 * OsmSinkNode constructor.
	 *
	 * @param nodeId id of the associated osm node
	 */
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
