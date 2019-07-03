package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.Collection;

/**
 * Represents a sink node that might be used as agent destination, with a given
 * set of source nodes.
 *
 * <p>
 * Notice that it is just a convenient way to represent origin / destinations
 * behavior, but agents can freely spawn and disappear anywhere.
 * </p>
 */
public interface SinkNode {

	/**
	 * Id of the associated node.
	 *
	 * @return associated node id
	 */
	public String getNodeId();

	/**
	 * Collection of source nodes from which this sink node can be reached.
	 *
	 * @return source node collections
	 */
	public Collection<SourceNode> sources();

}
