package smartgov.core.agent.core;

import smartgov.core.agent.core.behavior.AgentAction;
import smartgov.core.agent.core.behavior.Behavior;

/**
 * 
 * Abstract class used to represent a low level agent.
 *
 * This class represents the <i>mind</i> of the Agent, with its
 * defined behavior.
 *
 * The {@link smartgov.core.agent.core.AgentBody AgentBody} of the agent represents
 * its physical part, that can interact with the environment.
 * 
 * @author spageaud
 * @author pbreugnot
 *
 * @param <A> actions associated to this agent class.
 */
public abstract class Agent<A extends AgentAction> {
	
	private String id;
	private AgentBody<A> body;
	private Behavior<A> behavior;
	/**
	 * Agent constructor.
	 *
	 * @param id id that can be used to retrieve agents in the {@link smartgov.core.environment.SmartGovContext SmartGovContext}
	 * @param body body of this agent
	 * @param behavior of this agent
	 */
	public Agent(String id, AgentBody<A> body, Behavior<A> behavior){
		this.id = id;
		this.body = body;
		this.behavior = behavior;
		
		body.setAgent(this);
	}
	
	/**
	 * Agent id
	 *
	 * @return agent id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Agent body
	 *
	 * @return body of this agent
	 */
	public AgentBody<A> getBody(){
		return body;
	}
	
	/**
	 * Agent behavior
	 *
	 * @return behavior of this agent
	 */
	public Behavior<A> getBehavior() {
		return behavior;
	}

	/**
	 * Called each tick to make the agent live.
	 *
	 * <ol>
	 * 	<li> Call the Behavior {@link smartgov.core.agent.core.behavior.Behavior#provideAction provideAction()} method </li>
	 * 	<li> Make the AgentBody perform this action with the {@link smartgov.core.agent.core.AgentBody#doAction doAction()} method </li>
	 * </ol>
	 */
	public void live() {
		this.body.doAction(behavior.provideAction());
	}
}
