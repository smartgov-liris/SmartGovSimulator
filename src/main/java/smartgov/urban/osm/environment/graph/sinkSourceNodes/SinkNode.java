package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.Collection;

public interface SinkNode {

	public String getNodeId();
	public Collection<SourceNode> sources();

}
