package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.environment.graph.Arc;
import smartgov.core.output.agent.AgentBodyListIdSerializer;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.utils.LatLon;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
import smartgov.urban.osm.utils.OneWayDeserializer;

/**
 * A Road is a particular OsmWay where agents can move.
 *
 */
public class Road extends OsmWay {
	
	/**
	 * Describe the <i>oneway</i> status of a road, according
	 * to the official <a
	 * href="https://wiki.openstreetmap.org/wiki/Key:oneway">OSM tag documentation</a>.
	 */
	public enum OneWay {
		/**
		 * Corresponds to <i>highway="no"</i>, or any other situation
		 * that does not correspond to <code>YES</code> or
		 * <code>REVERSED</code>. Default value when no <i>highway</i>
		 * tag is specified.
		 */
		NO,
		
		/**
		 * Corresponds to <i>highway="no"</i>
		 */
		YES,
		
		/**
		 * Corresponds to <i>highway="-1"</i>
		 */
		REVERSED
	}
	
	private boolean oneway;
	
	private ArrayList<OsmAgentBody> forwardAgents;
	private ArrayList<OsmAgentBody> backwardAgents;
	
	/**
	 * JsonCreator used to load Roads from a Json file.
	 *
	 * <p>
	 * Can also be used to create roads manually.
	 * </p>
	 *
	 * @param id road id
	 * @param nodeRefs an ordered list of node ids
	 */
	@JsonCreator
	public Road(
		@JsonProperty("id") String id,
		@JsonProperty("nodeRefs") List<String> nodeRefs){
		super(id, nodeRefs);
		this.forwardAgents = new ArrayList<>();
		this.backwardAgents = new ArrayList<>();
	}
	
	/*
	 * Used to reverse the node list, in the case that the oneway is
	 * reversed. This correspond to the situation when "oneway=-1" in
	 * OSM, when the road is oneway but in the opposite order of its node
	 * list. It's a rare situation, but we handle it reversing the node
	 * list to represent the reversed oneway road as a normal oneway road.
	 */
	private void reverseNodeList() {
		List<String> nodesCopy = new ArrayList<>(this.getNodes());
		this.getNodes().clear();
		for (int i = nodesCopy.size() - 1; i>= 0; i--) {
			this.getNodes().add(nodesCopy.get(i));
		}
	}
	
	/*
	 * Smart (or hacky?) solution to load the oneway BOOLEAN from
	 * the Json file.
	 * In the json file, tags are represented as an Object, and
	 * "oneway: "yes"" might be one of them. But we want to represent this
	 * attribute as a boolean. So what we do is that we give the "tags"
	 * Object to a custom deserializer that will return a OneWay value from
	 * the "tags" object, that we handle in this function.
	 */
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
	
	/**
	 * If the road is oneway, the graph will only include arcs that connect
	 * road nodes in order. Else, arcs for the reverse nodes order will
	 * also be created.
	 *
	 * @return true if the road is oneway
	 */
	public boolean isOneway() {
		return oneway;
	}
	
	/**
	 * Computes the distance between the specified agent and its leader on
	 * the road.
	 *
	 * @param agent follower agent
	 * @return distance between leader and follower, in meter
	 */
	public double distanceBetweenAgentAndLeader(OsmAgentBody agent){
		return distanceBetweenTwoAgents(leaderOfAgent(agent), agent);
	}
	
	/**
	 * Adds an agent to the road. 
	 * <p>
	 * The direction to which the agent should be added is determined by
	 * the direction of its current arc, retrieved from its plan.
	 * </p>
	 * <p>
	 * Notice that the agent does not necessarily need to be at the origin
	 * of the road to use this function. If it's added from a node in the
	 * middle of the road, its position will be computed accordingly so
	 * that the follower / leader system keeps consistent.
	 * </p>
	 *
	 * @param agentBody body of the agent entering the road
	 */
	public void addAgent(OsmAgentBody agentBody) {
		/*
		 * Forces agent removal before adding him.
		 * Normally, this should be unnecessary.
		 * But in some cases, such as when an agent reached its destination,
		 * so it is removed from the road, but the behavior re-initialization add
		 * him again and we don't really know in which order the events occurred,
		 * the agent could be added just before it is removed, what caused 
		 * problems. So we remove it there to be sure, because it's better to
		 * remove him once,
		 * 
		 */
//		forwardAgents.remove(agentBody);
//		backwardAgents.remove(agentBody);
		
		int agentIndex;
		
		switch(((OsmArc) agentBody.getPlan().getCurrentArc()).getRoadDirection()) {
		case FORWARD:
			agentIndex = forwardAgents.size();
			while(
					agentIndex >= 1 &&
					getNodes().indexOf(forwardAgents.get(agentIndex - 1).getPlan().getCurrentNode().getId()) <
					getNodes().indexOf(agentBody.getPlan().getCurrentNode().getId())) {
						agentIndex--;
					}
			forwardAgents.add(agentIndex, agentBody);
			break;
			
		case BACKWARD:
			agentIndex = backwardAgents.size();
			while(
					agentIndex >= 1 &&
					getNodes().indexOf(backwardAgents.get(agentIndex - 1).getPlan().getCurrentNode().getId()) >
					getNodes().indexOf(agentBody.getPlan().getCurrentNode().getId())) {
						agentIndex--;
					}
			backwardAgents.add(agentIndex, agentBody);
		}
		agentBody.setCurrentRoad(this);
	}
	
	/**
	 * Removes an agent from this road.
	 *
	 * @param agentBody body of the agent to remove
	 */
	public void removeAgent(OsmAgentBody agentBody) {
		// Try to remove from the forward direction
		if(!forwardAgents.remove(agentBody)) {
			// if not in the forward direction, removes from the backward direction
			if(!backwardAgents.remove(agentBody)) {
				throw new IllegalStateException("Agent is not on the road.");
			};
		}
	}

	/**
	 * An ordered list of agents on this road in the forward direction.
	 * The first agent of the list is the leader of all the others, so that
	 * the first item of the list corresponds to the first agent of
	 * the road in terms of circulation.
	 *
	 * @return an umodifiable representation of the agents on the road in
	 * the forward direction
	 */
	@JsonSerialize(using = AgentBodyListIdSerializer.class)
	public List<OsmAgentBody> getForwardAgents() {
		return Collections.unmodifiableList(forwardAgents);
		
	}

	/**
	 * An ordered list of agents on this road in the backward direction.
	 * The first agent of the list is the leader of all the others, so that
	 * the first item of the list corresponds to the first agent of
	 * the road in terms of circulation.
	 *
	 * @return an umodifiable representation of the agents on the road in
	 * the backward direction
	 */
	@JsonSerialize(using = AgentBodyListIdSerializer.class)
	public List<OsmAgentBody> getBackwardAgents() {
		return Collections.unmodifiableList(backwardAgents);
	}
	
	private static OsmAgentBody leaderOfAgent(MovingAgentBody agent, ArrayList<OsmAgentBody> agentsOnRoad) {
		int agentPosition = agentsOnRoad.indexOf(agent);
		if (agentPosition < 0) {
			throw new IllegalArgumentException("The agent is not on this road.");
		}
		if(agentPosition == 0){
			//No leader if 0, not on the road if -1
			return null;
		}
		return agentsOnRoad.get(agentPosition - 1);
	}
	
	/**
	 * Returns the leader of the specified agent, in the direction
	 * determined by the direction of its current arc, according to the
	 * agent plan.
	 *
	 * @param agentBody follower agent
	 * @return leader agent of the specified agent on this road
	 */
	public OsmAgentBody leaderOfAgent(MovingAgentBody agentBody){
		switch(((OsmArc) agentBody.getPlan().getCurrentArc()).getRoadDirection()) {
		case FORWARD:
			return leaderOfAgent(agentBody, this.forwardAgents);
		case BACKWARD:
			return leaderOfAgent(agentBody, this.backwardAgents);
		default:
			return null;
		}
		
	}
	
	/**
	 * Compute the distance between two agents along the current road, taking into account road breaks
	 * (nodes and arcs between agents).
	 *
	 * <p>
	 * Computing the direct distance between two agents doesn't make much sense in general : use this
	 * function to compute the real distance between agents, along the road.
	 * </p>
	 * 
	 * <p>
	 * Also make sure that the follower is really located after the leader on the road.
	 * If leader and follower are not on this road, or are exchanged, unexpected things might happen.
	 * This could be hard checked in the function, but for efficiency purposes we will leave this
	 * responsibility to the user, at least for now.
	 * </p> 
	 *
	 * @param leader first agent
	 * @param follower second agent
	 * @return distance between the two agents, in meter
	 */
	public double distanceBetweenTwoAgents(OsmAgentBody leader, OsmAgentBody follower){
		switch(((OsmArc) follower.getPlan().getCurrentArc()).getRoadDirection()) {
		case FORWARD:
			if(forwardAgents.indexOf(follower) < 0) {
				throw new IllegalArgumentException("Follower is not on the road.");
			}
			if(forwardAgents.indexOf(leader) < 0) {
				throw new IllegalArgumentException("Leader is not on the road.");
			}
			break;
		case BACKWARD:
			if(backwardAgents.indexOf(follower) < 0) {
				throw new IllegalArgumentException("Follower is not on the road.");
			}
			if(backwardAgents.indexOf(leader) < 0) {
				throw new IllegalArgumentException("Leader is not on the road.");
			}
		}
		Arc leaderArc = leader.getPlan().getCurrentArc();
		Arc followerArc = follower.getPlan().getCurrentArc();
		
		if (leaderArc == followerArc) {
			// Agents are in the same arc, so distance between them is the direct distance along the arc
			return LatLon.distance(follower.getPosition(), leader.getPosition());
		}
		
		// There is at least one other node between the two agents, so we need to compute
		// the real distance between agents.
		Arc currentArc = followerArc;
		GeoNode currentTarget = (GeoNode) currentArc.getTargetNode();
		double distance = LatLon.distance(follower.getPosition(), currentTarget.getPosition()); 
		
		RoadDirection direction = ((OsmArc) follower.getPlan().getCurrentArc()).getRoadDirection();
		List<OsmArc> roadArcs;
		switch(direction) {
		case BACKWARD:
			roadArcs = backwardArcs;
			break;
		case FORWARD:
			roadArcs = forwardArcs;
			break;
		default:
			roadArcs = null;
			break;
		
		}
		int i = roadArcs.indexOf(currentArc);
		if(i < 0) {
			throw new IllegalStateException("Follower current arc doesn't seem to be on this road.");
		}
		while(i < roadArcs.size() - 1 && currentArc != leaderArc) {
			i++;
			// Get the next arc of the road
			currentArc = roadArcs.get(i);
			
			if(currentArc != leaderArc) {
				// A complete arc is between the two agents
				distance+=currentArc.getLength();
				currentTarget = (GeoNode) currentArc.getTargetNode();
			}
		}
		if (currentArc != leaderArc) {
			i = 0;
			while(i < roadArcs.size() && currentArc != leaderArc) {
				// Get the next arc of the road
				currentArc = roadArcs.get(i);
				
				if(currentArc != leaderArc) {
					// A complete arc is between the two agents
					distance+=currentArc.getLength();
					currentTarget = (GeoNode) currentArc.getTargetNode();
				}
				i++;
			}
		}
		if (currentArc != leaderArc) {
			throw new IllegalStateException("The leader has not been found.");
		}
		// Final part, between the last node and the leader
		distance+=LatLon.distance(currentTarget.getPosition(), leader.getPosition());
		return distance;
	}
	
	public OsmArc endOfRoad(RoadDirection direction) {
		switch(direction){
		case BACKWARD:
			return backwardArcs.get(backwardArcs.size() - 1);
		case FORWARD:
			return forwardArcs.get(forwardArcs.size() - 1);
		default:
			return null;
		}
	}
	
	
	private void checkArcIntegrity(
			String requiredSourceId,
			String requiredTargetId,
			List<OsmArc> arcsUnderTest) {
		OsmArc currentArc = arcsUnderTest.get(0);
		if (currentArc == null) {
			throw new IllegalStateException("Node " + requiredSourceId + " has no outgoing arcs registered in the road.");
		}
		String targetId = currentArc.getTargetNode().getId();
		if(!targetId.equals(requiredTargetId)) {
			throw new IllegalStateException(
					"Arc " + currentArc.getId()
					+ " from node " + requiredSourceId
					+ " should go to node " + requiredTargetId
					+ " but it goes to node " + targetId);
		}
		// This arc is valid
		arcsUnderTest.remove(currentArc);
	}
	
	public void checkIntegrity() {
		List<OsmArc> forwardArcs = new ArrayList<>(this.forwardArcs);
		
		for(int i = 0; i < getNodes().size() - 1; i++) {
			checkArcIntegrity(
					getNodes().get(i),
					getNodes().get(i + 1),
					forwardArcs
					);
		}
		
		if(forwardArcs.size() > 0) {
			Collection<String> arcIds = new ArrayList<>();
			for(OsmArc arc : forwardArcs) {
				arcIds.add(arc.getId());
			}
			throw new IllegalStateException("The following forward arcs does not match anything in this road : " + arcIds);
		}
		
		if(!isOneway()) {
			List<OsmArc> backwardArcs = new ArrayList<>(this.backwardArcs);
			
			for(int i = getNodes().size() - 1; i > 0; i--) {
				checkArcIntegrity(
						getNodes().get(i),
						getNodes().get(i - 1),
						backwardArcs
						);
			}
			
			if(backwardArcs.size() > 0) {
				Collection<String> arcIds = new ArrayList<>();
				for(OsmArc arc : backwardArcs) {
					arcIds.add(arc.getId());
				}
				throw new IllegalStateException("The following backward arcs does not match anything in this road : " + arcIds);
			}
		}
		
	}

	@Override
	public String toString() {
		return "Road [id=" + getId() + "]";
	}
}
