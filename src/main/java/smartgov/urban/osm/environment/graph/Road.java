package smartgov.urban.osm.environment.graph;

import java.util.List;
import java.util.Map;

import smartgov.core.environment.graph.Path;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.agent.OsmAgentBody;

/**
 * Stores a list of edges and common attributes of these edges
 * @author Simon
 *
 */
public class Road extends Path<OsmNode, OsmAgentBody> {

	private int lanes;
	
	public Road(String id, Map<String, String> attributes, List<OsmNode> nodes){
		super(id, attributes, nodes);
		if(this.attributes.containsKey("lanes")){
			this.lanes = Integer.valueOf(this.attributes.get("lanes"));
		}
	}
	
	
	public int getLanes() {
		return lanes;
	}
	
	
	public double getDistanceBetweenAgentAndLeader(OsmAgentBody agent){
		OsmAgentBody leader = getLeaderOfAgent(agent);
		if (leader != null) {
			return getDistanceBetweenTwoAgents(leader, agent);
		}
		else {
			return -1.0;
		}
	}
	
	public OsmAgentBody getLeaderOfAgent(OsmAgentBody agent){
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
	
}
