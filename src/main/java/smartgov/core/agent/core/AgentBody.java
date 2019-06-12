package smartgov.core.agent.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import smartgov.core.agent.core.behavior.AgentAction;

/**
 * An agent body represents its physical part, that is distinct from its <i>mind</i>.
 *
 * @see Agent
 * 
 * @author pbreugnot
 *
 */
@JsonIgnoreProperties({"agent", "agentMoveListeners", "nodeReachedListeners", "arcReachedListeners", "arcLeftListeners", "destinationReachedListeners"})
public abstract class AgentBody<A extends AgentAction> {

	private Agent<A> agent;
	
	/**
	 * Associates the specified agent to this body.
	 *
	 * @param agent agent to associate
	 */
	public void setAgent(Agent<A> agent) {
		this.agent = agent;
	}
	
	/**
	 * Agent associated to this body.
	 */
	public Agent<A> getAgent() {
		return agent;
	}
	
	/**
	 * Performs the specified action.
	 *
	 * @param action action to perform
	 */
	public abstract void doAction(A action);
}
