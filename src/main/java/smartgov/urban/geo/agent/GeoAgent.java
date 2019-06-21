package smartgov.urban.geo.agent;

import smartgov.core.agent.moving.MovingAgent;
import smartgov.core.agent.moving.behavior.MovingBehavior;

/**
 * An agent that moves in a geographical environment.
 */
public class GeoAgent extends MovingAgent {
	
	/**
	 * GeoAgent constructor.
	 *
	 * @param id agent id
	 * @param body body of this agent
	 * @param behavior moving behavior of this agent
	 */
	public GeoAgent(
			String id,
			GeoAgentBody body,
			MovingBehavior behavior) {
		super(id, body, behavior);
	}
}
