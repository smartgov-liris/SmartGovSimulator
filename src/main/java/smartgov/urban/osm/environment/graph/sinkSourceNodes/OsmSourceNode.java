package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.environment.graph.Arc;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmNode;

/**
 * A special OsmNode where agents appear.
 * 
 * For example, such nodes are built from nodes with no incoming arcs in 
 * provided examples.
 * 
 * @see OsmNode
 * @see SourceNode
 * 
 * @author pbreugnot
 *
 */
public class OsmSourceNode extends AbstractOsmSinkSourceNode implements SourceNode {

	public OsmSourceNode(
			OsmContext environment,
			String id,
			Coordinate coordinate,
			List<? extends Arc> outgoingArcs) {
		super(environment, id, coordinate);
		this.outgoingArcs.addAll(outgoingArcs);
	}
}
