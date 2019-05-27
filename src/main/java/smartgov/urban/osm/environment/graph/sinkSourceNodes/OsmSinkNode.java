package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.environment.graph.SinkNode;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;

/**
 * A special OsmNode where agents disappear.
 * 
 * For example, such nodes are built from nodes with no outgoing arcs in 
 * provided examples.
 * 
 * @see OsmNode
 * @see SinkNode
 * 
 * @author pbreugnot
 *
 */
public class OsmSinkNode extends AbstractOsmSinkSourceNode implements SinkNode {

	public OsmSinkNode(
			OsmContext environment,
			String id,
			Coordinate coordinate,
			List<OsmArc> incomingArcs) {
		super(environment, id, coordinate);
		this.incomingArcs = incomingArcs;
		
		registerSinkBehavior();
	}
}
