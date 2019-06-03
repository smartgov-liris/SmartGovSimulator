package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.environment.graph.SinkNode;
import smartgov.core.environment.graph.SourceNode;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.urban.osm.environment.OsmContext;

public class OsmSinkSourceNode extends AbstractOsmSinkSourceNode implements SinkNode, SourceNode {
	
	public OsmSinkSourceNode(
			OsmContext environment,
			String id,
			Coordinate coordinate,
			List<? extends Arc> incomingArcs,
			List<? extends Arc> outgoingArcs) {
		super(environment, id, coordinate);
		this.incomingArcs.addAll(incomingArcs);
		this.outgoingArcs.addAll(outgoingArcs);
		
		registerSinkBehavior();
	}
}
