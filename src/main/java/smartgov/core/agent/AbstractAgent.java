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
public abstract class AbstractAgent {
	
	protected String id;
	protected AbstractAgentBody body;
	protected AbstractBehavior behavior;
	
	public AbstractAgent(String id, AbstractAgentBody body, AbstractBehavior behavior){
		this.id = id;
		this.body = body;
		this.behavior = behavior;
	}

	/**
	 * Live method, called every tick. All the agent activity is in here.
	 */
//	@ScheduledMethod(start = 1, interval = 1)
//	public abstract void live();
	
	public AbstractAgentBody getBody(){
		return body;
	}
	
	public void setBody(AbstractAgentBody body) {
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
