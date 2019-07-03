package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Osm implementation of a source node.
 */
public class OsmSourceNode implements SourceNode {
	
	private String nodeId;
	private Collection<SinkNode> destinations;

	/**
	 * OsmSourceNode constructor.
	 *
	 * @param nodeId id of the associated osm node
	 */
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
