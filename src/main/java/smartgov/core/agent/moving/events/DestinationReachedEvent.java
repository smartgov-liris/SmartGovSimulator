package smartgov.core.agent.moving.events;

import smartgov.core.environment.graph.Node;

/**
 * Event triggered when an Agent reached his destination, defined in
 * its {@link smartgov.core.agent.moving.Plan Plan}. More exactly, the event
 * is triggered when pathComplete is set to true in the Plan.
 * 
 * @author pbreugnot
 *
 */
public class DestinationReachedEvent extends NodeReachedEvent {

	public DestinationReachedEvent(Node destination) {
		super(destination);
	}
	

}
