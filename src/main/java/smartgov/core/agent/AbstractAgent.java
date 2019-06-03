package smartgov.core.agent;

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
	
	public AbstractAgent(String id, AbstractAgentBody body){
		this.id = id;
		this.body = body;
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
	public abstract void initialize();
	// public abstract void recycleAgent(int id);
	
	public abstract void live();
}
