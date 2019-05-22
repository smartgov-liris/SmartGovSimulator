package smartgov.core.agent.perception;

import smartgov.core.agent.AbstractAgentBody;
import smartgov.core.environment.Perceivable;

/**
 * The frustum represents the vision of the agent, or the way agent can
 * perceive his environment.
 * @author spageaud
 *
 */
public abstract class AbstractSensor {
	
	public abstract Perception getPerceptions(Perceivable perceivableObject, AbstractAgentBody<?, ?, ?> agentBody);
	
}
