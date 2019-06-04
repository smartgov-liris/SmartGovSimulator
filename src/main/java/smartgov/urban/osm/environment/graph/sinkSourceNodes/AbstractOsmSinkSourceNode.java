package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import java.util.ArrayList;
import java.util.Collection;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.agent.Agent;
import smartgov.core.agent.AgentBody;
import smartgov.core.agent.MovingAgentBody;
import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.environment.graph.events.SinkAgentEvent;
import smartgov.core.environment.graph.events.SpawnAgentEvent;
import smartgov.core.events.EventHandler;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmNode;

public abstract class AbstractOsmSinkSourceNode extends OsmNode {

	protected OsmContext environment;
	
	private AbstractOsmSinkSourceNode closestDespawnNode;
	private Collection<EventHandler<SpawnAgentEvent>> spawnAgentListeners;
	private Collection<EventHandler<SinkAgentEvent>> sinkAgentListeners;
	
	
	public AbstractOsmSinkSourceNode(
			OsmContext environment,
			String id,
			Coordinate coordinate) {
		super(id, coordinate);
		this.environment = environment;
		this.spawnAgentListeners = new ArrayList<>();
		this.sinkAgentListeners = new ArrayList<>();
	}
	
	public void setClosestDespawnNode(AbstractOsmSinkSourceNode sinkNode) {
		this.closestDespawnNode = sinkNode;
	}

	public AbstractOsmSinkSourceNode getClosestDespawnNode() {
		return closestDespawnNode;
	}
	
	/**
	 * Sink behavior for regular applications
	 *
	 * When an Agent reach the SinkNode, it is reinitialized, but it is not removed
	 * from the Repast Context. Depending on its initialization process, it might be 
	 * added to an other SourceNode to respawn for example.   
	 */
	protected void registerSinkBehavior() {
		AbstractOsmSinkSourceNode thisNode = this;
		this.addAgentArrivalListener(new EventHandler<AgentArrival>() {

			@Override
			public void handle(AgentArrival event) {
				if(((MovingAgentBody) event.getAgent().getBody()).getPlan().isPathComplete()) {
					// Triggers sink listeners on this node.
					thisNode.triggerSinkAgentListeners(new SinkAgentEvent(event.getAgent()));
					
					// Agent reached the sink node, and this is its destination.
					AgentBody agentBody = event.getAgent().getBody();
					Agent agent = agentBody.getAgent();
					
					agent.initialize();
				}
			}
			
		});
	}
	
	/**
	 * Sink behavior when used in learning scenarios
	 */
	// TODO : if reused, must be registered with events.
//	protected void learningSinkBehavior() {
//		if(!environment.agents.isEmpty()){
//			for(OsmAgent<?, ?> agent : environment.agents.values()){
//				if(agent.getBody().getPosition().equals2D(this.getPosition())){
//					// TODO : Concurrent modification exception?
//					environment.agents.remove(agent.getId());
//					context.remove(agent.getBody());
//					context.remove(agent);
//					agent = null;
//				}
//			}
//		}
//	}
	
	public void addSpawnAgentListener(EventHandler<SpawnAgentEvent> spawnAgentListener) {
		spawnAgentListeners.add(spawnAgentListener);
	}
	
	public void triggerSpawnAgentListeners(SpawnAgentEvent event) {
		for (EventHandler<SpawnAgentEvent> listener : spawnAgentListeners) {
			listener.handle(event);
		}
	}
	
	public void addSinkAgentListener(EventHandler<SinkAgentEvent> sinkAgentListener) {
		sinkAgentListeners.add(sinkAgentListener);
	}
	
	public void triggerSinkAgentListeners(SinkAgentEvent event) {
		for (EventHandler<SinkAgentEvent> listener : sinkAgentListeners) {
			listener.handle(event);
		}
	}
}
