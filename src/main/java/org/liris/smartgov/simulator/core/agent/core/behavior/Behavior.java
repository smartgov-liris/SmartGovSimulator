package org.liris.smartgov.simulator.core.agent.core.behavior;

import org.liris.smartgov.simulator.core.agent.core.AgentBody;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstract class used to describe agent behaviors,
 * providing actions from a specified {@link org.liris.smartgov.simulator.core.agent.core.behavior.AgentAction AgentAction} 
 * implementation.
 *
 * @author spageaud, pbreugnot
 *
 * @param <A> AgentAction provided by this behavior. 
 */
public abstract class Behavior<A extends AgentAction> {

	@JsonIgnore
	private AgentBody<A> agentBody;
	/**
	 * Behavior constructor.
	 *
	 * @param agentBody AgentBody that will adopt this behavior.
	 */	
	public Behavior(AgentBody<A> agentBody) {
		this.agentBody = agentBody;
	}
	
	public AgentBody<A> getAgentBody() {
		return agentBody;
	}

	/**
	 * Provide the actions that an agent should perform according to the current conditions.
	 *
	 * This function is called at each tick to determine what action the {@link org.liris.smartgov.simulator.core.agent.core.AgentBody AgentBody}
	 * should perform. The function is then handled and executed by the {@link org.liris.smartgov.simulator.core.agent.core.AgentBody#doAction AgentBody doAction}
	 * function.
	 *
	 * @author pbreugnot
	 * 
	 * @return an action to perform
	 */
	public abstract A provideAction();
	
}
