package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
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
	
	public double distanceBetweenAgentAndLeader(OsmAgentBody agent){
		return distanceBetweenTwoAgents(leaderOfAgent(agent), agent);
	}
	
	public void addAgent(OsmAgentBody agentBody) {
		int agentIndex;
		
		switch(((OsmArc) agentBody.getPlan().getCurrentArc()).getRoadDirection()) {
		case FORWARD:
			agentIndex = forwardAgentsOnRoad.size();
			while(
					agentIndex >= 1 &&
					getNodes().indexOf(forwardAgentsOnRoad.get(agentIndex - 1).getPlan().getCurrentNode().getId()) <
					getNodes().indexOf(agentBody.getPlan().getCurrentNode().getId())) {
						agentIndex--;
					}
			forwardAgentsOnRoad.add(agentIndex, agentBody);
			break;
			
		case BACKWARD:
			agentIndex = backwardAgentsOnRoad.size();
			while(
					agentIndex >= 1 &&
					getNodes().indexOf(backwardAgentsOnRoad.get(agentIndex - 1).getPlan().getCurrentNode().getId()) >
					getNodes().indexOf(agentBody.getPlan().getCurrentNode().getId())) {
						agentIndex--;
					}
			backwardAgentsOnRoad.add(agentIndex, agentBody);
		}
	}
	
	public void removeAgent(OsmAgentBody agentBody, RoadDirection direction) {
		switch(direction) {
		case FORWARD:
			forwardAgentsOnRoad.remove(agentBody);
			break;
		case BACKWARD:
			backwardAgentsOnRoad.remove(agentBody);
		}
	}

//	public void addForwardAgentToRoad(OsmAgentBody agentBody) {
//		forwardAgentsOnRoad.add(agentBody);
//	}
//	
//	public void addBackwardAgentToRoad(OsmAgentBody agentBody) {
//		backwardAgentsOnRoad.add(agentBody);
//	}
	
	public List<OsmAgentBody> getAgentsOnRoad(RoadDirection direction) {
		switch(direction) {
		case FORWARD:
			return Collections.unmodifiableList(forwardAgentsOnRoad);
		case BACKWARD:
			return Collections.unmodifiableList(backwardAgentsOnRoad);
		default:
			return null;
		}
		
	}
	
	private static OsmAgentBody leaderOfAgent(MovingAgentBody agent, ArrayList<OsmAgentBody> agentsOnRoad) {
		int agentPosition = agentsOnRoad.indexOf(agent);
		if(agentPosition == 0){
			//No leader if 0, not on the road if -1
			return null;
		}
		return agentsOnRoad.get(agentPosition - 1);
	}
	
	public OsmAgentBody leaderOfAgent(MovingAgentBody agentBody){
		switch(((OsmArc) agentBody.getPlan().getCurrentArc()).getRoadDirection()) {
		case FORWARD:
			return leaderOfAgent(agentBody, this.forwardAgentsOnRoad);
		case BACKWARD:
			return leaderOfAgent(agentBody, this.backwardAgentsOnRoad);
		default:
			return null;
		}
		
	}
	
	public double distanceBetweenTwoAgents(OsmAgentBody leader, OsmAgentBody follower){
		if (leader != null) {
			return GISComputation.GPS2Meter(leader.getPosition(), follower.getPosition());
		}
		else {
			return -1.0;
		}
	}

	@Override
	public String toString() {
		return "Road [id=" + getId() + "]";
	}
	
	
}
