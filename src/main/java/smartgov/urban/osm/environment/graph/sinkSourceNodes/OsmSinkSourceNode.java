package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.environment.graph.SinkNode;
import smartgov.core.environment.graph.SourceNode;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmArc;

public class OsmSinkSourceNode extends AbstractOsmSinkSourceNode implements SinkNode, SourceNode {
	
	public OsmSinkSourceNode(
			OsmContext environment,
			String id,
			Coordinate coordinate,
			List<OsmArc> incomingArcs,
			List<OsmArc> outgoingArcs) {
		super(environment, id, coordinate);
		this.incomingArcs = incomingArcs;
		this.outgoingArcs = outgoingArcs;
		
		registerSinkBehavior();
	}
}
