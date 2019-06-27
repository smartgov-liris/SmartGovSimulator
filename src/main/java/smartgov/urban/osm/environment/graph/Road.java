package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.List;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.agent.OsmAgentBody;

/**
 * Stores a list of edges and common attributes of these edges
 * @author Simon
 *
 */
public class Road {

	private String id;
	
	// TODO : Must be unserialized with a custom deserializer.
	private List<OsmNode> nodes;
	
	// TODO : Serialize ?
	private ArrayList<OsmAgentBody> agentsOnRoad;
	
	public Road(String id, List<OsmNode> nodes){
		this.id = id;
		this.nodes = nodes;
		this.agentsOnRoad = new ArrayList<>();
	}

	public String getId() {
		return id;
	}
	
	public List<OsmNode> getNodes() {
		return nodes;
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
	public ArrayList<OsmAgentBody> getAgentsOnPath() {
		return agentsOnRoad;
	}
	
	public OsmAgentBody leaderOfAgent(MovingAgentBody agent){
		int agentPosition = this.getAgentsOnPath().indexOf(agent);
		if(agentPosition <= 0){
			//No leader if 0, not on the road if -1
			return null;
		}
		return this.getAgentsOnPath().get(agentPosition - 1);
	}
	
	public double distanceBetweenTwoAgents(OsmAgentBody leader, OsmAgentBody follower){
		return GISComputation.GPS2Meter(leader.getPosition(), follower.getPosition());
	}
}
