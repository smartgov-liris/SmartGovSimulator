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
public abstract class AbstractBehavior<B extends AbstractAgentBody<?>> {
	
	private B agentBody;
	
	public AbstractBehavior(B agentBody) {
		this.agentBody = agentBody;
	}
	
	public B getAgentBody() {
		return agentBody;
	}

	/**
	 * Provide the actions that a lower agent should perform according to the current conditions.
	 * 
	 * @author pbreugnot
	 * 
	 * @param perceptions
	 * @param properties
	 * @param body
	 * @return
	 */
	public abstract LowLevelAction provideAction();
	
	public abstract void initialize();
	
}
