package smartgov.core.environment.graph;

import smartgov.core.environment.graph.events.SinkAgentEvent;

/**
 * A special node behavior, where agents disappear.
 * 
 * @author pbreugnot
 *
 */
public interface SinkNode {
	
	public String getId();
	
	public void triggerSinkAgentListeners(SinkAgentEvent event);
}
