package smartgov.core.environment;

import smartgov.core.agent.AbstractAgentBody;
import smartgov.core.agent.perception.Perception;

/**
 * Implementing this interface allows the agent to perceive the current object as part of the environment.
 * @author spageaud
 *
 */
public interface Perceivable {

	Perception perceivedObject(AbstractAgentBody<?, ?, ?> agentBodyAbstract);
	
}
