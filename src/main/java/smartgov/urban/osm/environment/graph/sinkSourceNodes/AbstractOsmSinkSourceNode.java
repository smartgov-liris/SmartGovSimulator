package smartgov.urban.osm.environment.graph.sinkSourceNodes;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.Plan;
import smartgov.core.environment.graph.events.AgentArrival;
import smartgov.core.events.EventHandler;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmNode;

public abstract class AbstractOsmSinkSourceNode extends OsmNode {

	protected OsmContext environment;
	
	private AbstractOsmSinkSourceNode closestDespawnNode;
	
	public AbstractOsmSinkSourceNode(
			OsmContext environment,
			String id,
			Coordinate coordinate) {
		super(id, coordinate, null);
		this.environment = environment;
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
		this.addAgentArrivalListener(new EventHandler<AgentArrival>() {

			@Override
			public void handle(AgentArrival event) {
				Plan plan = ((MovingAgentBody) event.getAgent().getBody()).getPlan();
				if(plan != null && plan.isPathComplete()) {
					// Agent reached the sink node, and this is its destination.					
					((OsmAgent) event.getAgent()).initialize();
				}
			}
			
		});
	}
}
