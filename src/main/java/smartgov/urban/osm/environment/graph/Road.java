package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.agent.OsmAgentBody;

/**
 * Stores a list of edges and common attributes of these edges
 * @author Simon
 *
 */
public class Road extends OsmWay {
	
	// TODO : Serialize ?
	private ArrayList<OsmAgentBody> agentsOnRoad;
	
	@JsonCreator
	public Road(
		@JsonProperty("id") String id,
		@JsonProperty("nodeRefs") List<String> nodeRefs){
		super(id, nodeRefs);
		this.agentsOnRoad = new ArrayList<>();
	}
	
	public double distanceBetweenAgentAndLeader(OsmAgentBody agent){
		OsmAgentBody leader = (OsmAgentBody) leaderOfAgent(agent);
		if (leader != null) {
			return distanceBetweenTwoAgents(leader, agent);
		}
		else {
			return -1.0;
		}
	}

	public void addAgentToPath(OsmAgentBody agentBody) {
		agentsOnRoad.add(agentBody);
	}
	public ArrayList<OsmAgentBody> getAgentsOnRoad() {
		return agentsOnRoad;
	}
	
	public OsmAgentBody leaderOfAgent(MovingAgentBody agent){
		int agentPosition = this.getAgentsOnRoad().indexOf(agent);
		if(agentPosition <= 0){
			//No leader if 0, not on the road if -1
			return null;
		}
		return this.getAgentsOnRoad().get(agentPosition - 1);
	}
	
	public double distanceBetweenTwoAgents(OsmAgentBody leader, OsmAgentBody follower){
		return GISComputation.GPS2Meter(leader.getPosition(), follower.getPosition());
	}
}
