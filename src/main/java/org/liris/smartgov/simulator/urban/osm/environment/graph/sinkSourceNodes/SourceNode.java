package org.liris.smartgov.simulator.urban.osm.environment.graph.sinkSourceNodes;

import java.util.Collection;

/**
 * Represents a source node that might be used as agent origin, with a given
 * set of destination sink nodes.
 *
 * <p>
 * Notice that it is just a convenient way to represent origin / destinations
 * behavior, but agents can freely spawn and disappear anywhere.
 * </p>
 */
public interface SourceNode {
	
	/**
	 * Id of the associated node.
	 *
	 * @return associated node id
	 */
	public String getNodeId();

	/**
	 * A collection of destinations that can be reached from this source.
	 *
	 * @return destinations collection
	 */
	public Collection<SinkNode> destinations();

}
