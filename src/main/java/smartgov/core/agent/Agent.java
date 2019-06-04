package smartgov.core.agent;

import smartgov.core.agent.behavior.AbstractBehavior;

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
public abstract class Agent<A extends Enum<?>> {
	
	protected String id;
	protected AgentBody<A> body;
	protected AbstractBehavior<A> behavior;
	
	public Agent(String id, AgentBody<A> body, AbstractBehavior<A> behavior){
		this.id = id;
		this.body = body;
		this.behavior = behavior;
	}
	
	public AgentBody<A> getBody(){
		return body;
	}
	
	public void setBody(AgentBody<A> body) {
		this.body = body;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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
