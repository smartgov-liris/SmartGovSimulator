package org.liris.smartgov.simulator.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.output.agent.AgentBodyListIdSerializer;
import org.liris.smartgov.simulator.urban.geo.environment.graph.GeoNode;
import org.liris.smartgov.simulator.urban.geo.utils.LatLon;
import org.liris.smartgov.simulator.urban.osm.agent.OsmAgentBody;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc.RoadDirection;
import org.liris.smartgov.simulator.urban.osm.environment.graph.tags.Highway;
import org.liris.smartgov.simulator.urban.osm.environment.graph.tags.Oneway;
import org.liris.smartgov.simulator.urban.osm.environment.graph.tags.OsmTag;
import org.liris.smartgov.simulator.urban.osm.environment.graph.tags.Service;
import org.liris.smartgov.simulator.urban.osm.utils.OsmTagsDeserializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A Road is a particular OsmWay where agents can move.
 *
 */
public class Road extends OsmWay {
	
	private boolean oneway;
	
	@JsonSerialize(using = OsmTag.OsmTagSerializer.class)
	private Highway highway = Highway.UNCLASSIFIED;
	@JsonSerialize(using = OsmTag.OsmTagSerializer.class)
	private Service service = Service.NONE;
	
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
	 * Special function, automatically called by Jackson
	 * when deserializing roads, that will build an osm
	 * tag map from the json "tags" entry.
	 * 
	 * Used to deserialize the Oneway and Highway fields.
	 * 
	 */
	@JsonDeserialize(using = OsmTagsDeserializer.class)
	@JsonProperty("tags")
	private void processTags(Map<String, OsmTag> tags) {
		Oneway oneway = (Oneway) tags.get("oneway");
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
		
		highway = (Highway) tags.get("highway");
		service = (Service) tags.get("service");
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
	 * Sets the oneway attribute of this road.
	 * 
	 * <p>
	 * Might be used to replace the default OSM value
	 * when fixing dead ends for example.
	 * </p>
	 * 
	 * @param oneway oneway value
	 */
	public void setOneway(boolean oneway) {
		this.oneway = oneway;
	}
	
	/**
	 * Returns the highway type of this road.
	 * 
	 * @return highway type
	 */
	public Highway getHighway() {
		return highway;
	}
	
	/**
	 * Returns the service type of this road, if any.
	 * Default value set to NONE.
	 * 
	 * @return service type
	 */
	public Service getService() {
		return service;
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
	 * <p>
	 * This method is <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html">
	 * synchronized</a>, so that multiple threads can safely add agents on the same road concurrently.
	 * </p>
	 *
	 * @param agentBody body of the agent entering the road
	 */
	public synchronized void addAgent(OsmAgentBody agentBody) {
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
