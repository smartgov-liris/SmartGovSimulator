package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.Collection;

public interface SourceNode {
	
	public String getNodeId();
	public Collection<SinkNode> destinations();

}
