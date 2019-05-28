package smartgov.urban.osm.simulation.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import smartgov.core.agent.AbstractAgent;
import smartgov.core.environment.SmartGovContext;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.agent.actuator.CarMover;
import smartgov.urban.osm.agent.behavior.BasicBehavior;
import smartgov.urban.osm.agent.properties.OsmAgentProperties;
import smartgov.urban.osm.environment.OsmContext;

/**
 * Creates an osm environment with human agents without any particular objective.
 * 
 * @author pbreugnot
 * @author Simon
 *
 */
public class ScenarioLowAgents extends ScenarioVisualization {
	
	public static final String name = "LowAgents";

	public ScenarioLowAgents(OsmContext environment) {
		super(environment);
	}

//	@Override
//	public OsmAgent<BasicProperties, BasicBehavior> createAnAgentWithID(String id, Geography<Object> geography, AbstractOsmSinkSourceNode sourceNode) {
//		
//		OsmAgentBody body = (OsmAgentBody) environment.bodiesStock.poll();
//		
//		sourceNode.getOutgoingArcs().get(0).getRoad().getAgentIds().add(id);
//		
//		BasicProperties properties = new BasicProperties(environment);
//		OsmAgent<BasicProperties, BasicBehavior> agent = new OsmAgent<>(id, body, new BasicBehavior(), properties, new CommuterPerception());
//		body.setAgent(agent);
//		
//		agent.initialize();
//		
//		body.setSpeed(30.0);
//		return agent;
//	}

//	@Override
//	public Context<Object> loadWorld(Context<Object> context) {
//		super.loadWorld(context);
//		
//		createOrientedGraph();
//		createSourceAndSinkNodes(context);
//		
//		OsmEnvironment.AGENT_MAX = Integer.parseInt(environment.configFile.get("agent_number"));
////		createBodies(context, geography, environment.carDriverSensor);
//		createAgents(context);
//		
//		return context;
//	}

//	@Override
//	public Collection<Collection<?>> elementsToAddToContext() {
//		Collection<Collection<?>> elementsToAdd = new ArrayList<>();
//		elementsToAdd.add(environment.nodes.values());
//		elementsToAdd.add(environment.sourceNodes.values());
//		elementsToAdd.add(environment.sinkNodes.values());
//		elementsToAdd.add(environment.arcs.values());
//		elementsToAdd.add(environment.buildings);
//
//		List<Object> spots = new ArrayList<>();
//		for(int i = 0; i < environment.parkingSpots.size(); i++){
//			if(!environment.parkingSpots.get(i).isFailed()){
//				spots.add(environment.parkingSpots.get(i));
//			}
//		}
//		elementsToAdd.add(spots);
//		
//		return elementsToAdd;
//	}
	
	/**
	 * Create some basic agents that move in the city, from random 
	 * source nodes to random sink nodes.
	 * 
	 * A body from the environment bodies stock is associated to each agents,
	 * so bodies should have been initialized first.
	 * <strong>Will probably be the object of a future refactoring.</strong>
	 * 
     * Even if agents move, spawn, despawn, respawn, they are only
	 * added once to the context.
	 * 
	 * This allows us to re-use agents and their bodies instead of
	 * destroy them, remove them from the context, re-build and
	 * re-add them to the context. 
	 */
	@Override
	public Collection<AbstractAgent<?>> buildAgents() {
		Collection<AbstractAgent<?>> agents = new ArrayList<>();
		int agentCount = Integer.valueOf((String) getOsmContext().getConfig().get("AgentNumber"));
		for(int i = 0; i < agentCount; i++){
			OsmAgentProperties properties = new OsmAgentProperties(getOsmContext());
			
			OsmAgentBody body = createAgentBody(
					String.valueOf(i),
					getOsmContext());
			
			OsmAgent<OsmAgentProperties, BasicBehavior> newAgent = new OsmAgent<>(
					String.valueOf(i),
					body,
					new BasicBehavior(body),
					properties);

			newAgent.initialize();
			agents.add(newAgent);
		}
		return agents;
	}
	
	/**
	 * Public method that behaves as an AgentBody factory.
	 * 
	 * Can be overridden by sub-classes to give new bahaviors to
	 * agent bodies.
	 * 
	 * @param id AgentBody id
	 * @param geography Current geography
	 * @param environment Current OsmEnvironment
	 * @return Created OsmAgentBody
	 */
	public OsmAgentBody createAgentBody(
			String id,
			OsmContext environment) {
		return new OsmAgentBody(
				id,
				new CarMover(),
				environment);
	}
}
