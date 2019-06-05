package smartgov.core.agent.core;

import smartgov.core.agent.core.behavior.AgentAction;
import smartgov.core.agent.core.behavior.Behavior;

//import repast.simphony.engine.schedule.ScheduledMethod;

/**
 * 
 * Abstract class to represent a LowLayer agent. The body part of the agents represents
 * its physical. (eg: what will be displayed on a map for a given agent in a city)
 * 
 * @author Simon
 * @author pbreugnot
 *
 * @param <B> Body type of the agent.
 */
public abstract class Agent<A extends AgentAction> {
	
	protected String id;
	protected AgentBody<A> body;
	protected Behavior<A> behavior;
	
	public Agent(String id, AgentBody<A> body, Behavior<A> behavior){
		this.id = id;
		this.body = body;
		this.behavior = behavior;
		
		body.setAgent(this);
	}
	
	public String getId() {
		return id;
	}
	
	public AgentBody<A> getBody(){
		return body;
	}
	
	public Behavior<A> getBehavior() {
		return behavior;
	}

	/**
	 * This function is called to initialize, or re-initialize an agent. It is notably 
	 * used on SinkNodes, to re-initialized agents without removing them from the Context. 
	 */
	public void initialize() {
		body.initialize();
	}
	// public abstract void recycleAgent(int id);
	
	public void live() {
		this.body.doAction(behavior.provideAction());
	}
}
