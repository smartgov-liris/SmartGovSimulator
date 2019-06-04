package smartgov.core.agent.behavior;

import smartgov.core.agent.AbstractAgentBody;
import smartgov.core.environment.LowLevelAction;

/**
 * Provide an action for specified perceptions, properties and agent body.
 * 
 * @author Simon
 *
 * @param <T> AbstractPerception
 * @param <W> AbstractProperties
 * @param <B> AbstractAgentBody
 */
public abstract class AbstractBehavior {
	
	private AbstractAgentBody agentBody;
	
	public AbstractBehavior(AbstractAgentBody agentBody) {
		this.agentBody = agentBody;
	}
	
	public AbstractAgentBody getAgentBody() {
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
	public abstract LowLevelAction provideAction();
	
}
