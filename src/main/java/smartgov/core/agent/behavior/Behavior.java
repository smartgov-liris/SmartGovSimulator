package smartgov.core.agent.behavior;

import smartgov.core.agent.AgentBody;

/**
 * Provide an action for specified perceptions, properties and agent body.
 * 
 * @author Simon
 *
 * @param <T> AbstractPerception
 * @param <W> AbstractProperties
 * @param <B> AbstractAgentBody<A>
 */
public abstract class Behavior<A extends AgentAction> {
	
	private AgentBody<A> agentBody;
	
	public Behavior(AgentBody<A> agentBody) {
		this.agentBody = agentBody;
	}
	
	public AgentBody<A> getAgentBody() {
		return agentBody;
	}

	/**
	 * Provide the actions that a lower agent should perform according to the current conditions.
	 * 
	 * @author pbreugnot
	 * 
	 * @param perceptions
	 * @return
	 */
	public abstract A provideAction();
	
}
