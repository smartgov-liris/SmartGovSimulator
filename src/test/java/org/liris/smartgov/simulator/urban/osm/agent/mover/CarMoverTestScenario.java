package org.liris.smartgov.simulator.urban.osm.agent.mover;

import java.util.ArrayList;
import java.util.Collection;

import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.agent.moving.behavior.TestMovingBehavior;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.urban.osm.agent.OsmAgent;
import org.liris.smartgov.simulator.urban.osm.agent.OsmAgentBody;
import org.liris.smartgov.simulator.urban.osm.agent.mover.CarMover;
import org.liris.smartgov.simulator.urban.osm.scenario.BasicOsmScenario;

/**
 * Basic OSM scenario with only four nodes, linked into a loop.
 * Two agents will live in this loop, and we will monitor their
 * speeds to check the CarMover and the Gipps' model.
 */
public class CarMoverTestScenario extends BasicOsmScenario {


	public static final String name = "carMoverTest";
	public static final Double maximumAcceleration = 4.0;
	public static final Double maximumBraking = -6.0;
	public static final Double vehicleSize = 6.0;
	public static final Double leaderMaxSpeed = 8.3; // 30km/h 
	// public static final Double followerMaxSpeed = 12.5; // 45 km/h 
	public static final Double followerMaxSpeed = 15.; // 45 km/h 

	

	@Override
	public Collection<Agent<?>> buildAgents(SmartGovContext context) {
		Collection<Agent<?>> agents = new ArrayList<>();
		
		// Follower agent, driving after the leader and faster
		CarMover mover = new CarMover(maximumAcceleration, maximumBraking, followerMaxSpeed, vehicleSize);
		
		OsmAgentBody agentBody = new OsmAgentBody(mover);

		OsmAgent osmAgent = new OsmAgent(
				"1",
				agentBody,
				new TestMovingBehavior(
						agentBody,
						context.nodes.get("1"),
						context.nodes.get("3"),
						context));
		
		// Leader agent (slow)
		mover = new CarMover(maximumAcceleration, maximumBraking, leaderMaxSpeed, vehicleSize);
		agentBody = new OsmAgentBody(mover);
		mover.setAgentBody(agentBody);
		
		agentBody.setSpeed(0);
		
		agents.add(osmAgent);

		osmAgent = new OsmAgent(
				"2",
				agentBody,
				new TestMovingBehavior(
						agentBody,
						context.nodes.get("2"),
						context.nodes.get("4"),
						context));
		
		agents.add(osmAgent);
		
		return agents;
	}
	
}
