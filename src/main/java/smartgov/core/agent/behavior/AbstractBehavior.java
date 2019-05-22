package smartgov.core.agent.behavior;

import smartgov.core.agent.AbstractAgentBody;
import smartgov.core.agent.perception.AbstractPerception;
import smartgov.core.agent.properties.AbstractProperties;
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
public abstract class AbstractBehavior<T extends AbstractPerception, W extends AbstractProperties, B extends AbstractAgentBody<?, ?, ?>> {

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
	public abstract LowLevelAction provideAction(T perceptions, W properties, B body);
	
	public abstract void initialize();
	
	public abstract void setInitialState();
	
	public abstract void setToFinalState();
	
}
