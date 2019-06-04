package smartgov.core.agent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An agent body represents its physical part, that is distinct from its "mind".
 * @see Agent
 * 
 * @author pbreugnot
 *
 */
@JsonIgnoreProperties({"agent", "agentMoveListeners", "nodeReachedListeners", "arcReachedListeners", "arcLeftListeners", "destinationReachedListeners"})
public abstract class AgentBody<A extends Enum<?>> {

	protected Agent<A> agent;
	
	public void setAgent(Agent<A> agent) {
		this.agent = agent;
	}
	
	public Agent<A> getAgent() {
		return agent;
	}
	
	public abstract void initialize();
	
	public abstract void doAction(A action);
}
