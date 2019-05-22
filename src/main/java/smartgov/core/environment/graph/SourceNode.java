package smartgov.core.environment.graph;

import smartgov.core.environment.graph.events.SpawnAgentEvent;

/**
 * A special node behavior, where agents appears.
 * 
 * @author pbreugnot
 *
 */
public interface SourceNode {
	
	public String getId();
	
	public void triggerSpawnAgentListeners(SpawnAgentEvent event);
}
