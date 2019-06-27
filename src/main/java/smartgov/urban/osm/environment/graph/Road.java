package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.utils.OneWayDeserializer;

/**
 * Stores a list of edges and common attributes of these edges
 * @author Simon
 *
 */
public class Road extends OsmWay {
	
	public enum OneWay {NO, YES, REVERSED}
	
	private boolean oneway;
	
	// TODO : Serialize ?
	private ArrayList<OsmAgentBody> forwardAgentsOnRoad;
	private ArrayList<OsmAgentBody> backwardAgentsOnRoad;
	
	@JsonCreator
	public Road(
		@JsonProperty("id") String id,
		@JsonProperty("nodeRefs") List<String> nodeRefs){
		super(id, nodeRefs);
		this.forwardAgentsOnRoad = new ArrayList<>();
		this.backwardAgentsOnRoad = new ArrayList<>();
	}
	
	private void reverseNodeList() {
		List<String> nodesCopy = new ArrayList<>(this.getNodes());
		this.getNodes().clear();
		for (int i = nodesCopy.size() - 1; i>= 0; i--) {
			this.getNodes().add(nodesCopy.get(i));
		}
	}
	
	@JsonDeserialize(using = OneWayDeserializer.class)
	@JsonProperty("tags")
	private void processTags(OneWay oneway) {
		switch(oneway) {
		case NO:
			this.oneway = false;
			break;
		case REVERSED:
			this.oneway = true;
			reverseNodeList();
			break;
		case YES:
			this.oneway = true;
			break;
		default:
			break;
		
		}
	}
	
	public boolean isOneway() {
		return oneway;
	}
	
	public double forwardDistanceBetweenAgentAndLeader(OsmAgentBody agent){
		return distanceBetweenTwoAgents(forwardLeaderOfAgent(agent), agent);
	}
	
	public double backwardDistanceBetweenAgentAndLeader(OsmAgentBody agent){
		return distanceBetweenTwoAgents(backwardLeaderOfAgent(agent), agent);
	}

	public void addForwardAgentToRoad(OsmAgentBody agentBody) {
		forwardAgentsOnRoad.add(agentBody);
	}
	
	public void addBackwardAgentToRoad(OsmAgentBody agentBody) {
		backwardAgentsOnRoad.add(agentBody);
	}
	
	public ArrayList<OsmAgentBody> getForwardAgentsOnRoad() {
		return forwardAgentsOnRoad;
	}
	
	public ArrayList<OsmAgentBody> getBackwardAgentsOnRoad() {
		return backwardAgentsOnRoad;
	}
	
	private static OsmAgentBody leaderOfAgent(MovingAgentBody agent, ArrayList<OsmAgentBody> agentsOnRoad) {
		int agentPosition = agentsOnRoad.indexOf(agent);
		if(agentPosition <= 0){
			//No leader if 0, not on the road if -1
			return null;
		}
		return agentsOnRoad.get(agentPosition - 1);
	}
	
	public OsmAgentBody forwardLeaderOfAgent(MovingAgentBody agent){
		return leaderOfAgent(agent, this.forwardAgentsOnRoad);
	}
	
	public OsmAgentBody backwardLeaderOfAgent(MovingAgentBody agent){
		return leaderOfAgent(agent, this.backwardAgentsOnRoad);
	}
	
	public double distanceBetweenTwoAgents(OsmAgentBody leader, OsmAgentBody follower){
		if (leader != null) {
			return GISComputation.GPS2Meter(leader.getPosition(), follower.getPosition());
		}
		else {
			return -1.0;
		}
	}
}
