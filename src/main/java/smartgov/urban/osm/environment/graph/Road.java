package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.environment.graph.Node;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.agent.OsmAgentBody;

/**
 * Stores a list of edges and common attributes of these edges
 * @author Simon
 *
 */
public class Road {

	private String id;
	private List<? extends Node> nodes;
	private ArrayList<OsmAgentBody> agentsOnRoad;
	protected Map<String, String> attributes;
	private int lanes;
	
	public Road(String id, Map<String, String> attributes, List<OsmNode> nodes){
		this.id = id;
		this.nodes = nodes;
		this.attributes = attributes;
		if(this.attributes.containsKey("lanes")){
			this.lanes = Integer.valueOf(this.attributes.get("lanes"));
		}
		this.agentsOnRoad = new ArrayList<>();
	}

	public String getId() {
		return id;
	}
	
	public List<? extends Node> getNodes() {
		return nodes;
	}
	
	public int getLanes() {
		return lanes;
	}
	
	
	public double getDistanceBetweenAgentAndLeader(OsmAgentBody agent){
		OsmAgentBody leader = (OsmAgentBody) getLeaderOfAgent(agent);
		if (leader != null) {
			return getDistanceBetweenTwoAgents(leader, agent);
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
	
	public OsmAgentBody getLeaderOfAgent(MovingAgentBody agent){
		int agentPosition = this.getAgentsOnPath().indexOf(agent);
		if(agentPosition <= 0){
			//No leader if 0, not on the road if -1
			return null;
		}
		return this.getAgentsOnPath().get(agentPosition - 1);
	}
	
	public double getDistanceBetweenTwoAgents(OsmAgentBody leader, OsmAgentBody follower){
		return GISComputation.GPS2Meter(leader.getPosition(), follower.getPosition());
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
}
