package smartgov.core.agent;

import smartgov.core.agent.properties.AgentProperties;

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
public abstract class AbstractAgent<B extends AbstractAgentBody<?, ?, ?>> {
	
	protected String id;
	protected B body;
	protected AgentProperties agentProperties;
	
	public AbstractAgent(String id, B body, AgentProperties agentProperties){
		this.id = id;
		this.body = body;
		this.agentProperties = agentProperties;
	}

	/**
	 * Live method, called every tick. All the agent activity is in here.
	 */
//	@ScheduledMethod(start = 1, interval = 1)
//	public abstract void live();
	
	public B getBody(){
		return body;
	}
	
	public void setBody(B body) {
		this.body = body;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public AgentProperties getAgentProperties() {
		return agentProperties;
	}
	
	/**
	 * This function is called to initialize, or re-initialize an agent. It notably 
	 * used on SinkNodes, to re-initialized agents without remove them from the Repast Context. 
	 */
	public abstract void initialize();
	// public abstract void recycleAgent(int id);
	
	public abstract void live();
}
