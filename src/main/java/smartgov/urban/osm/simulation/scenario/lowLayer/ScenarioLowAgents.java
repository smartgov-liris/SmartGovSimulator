package smartgov.urban.osm.simulation.scenario.lowLayer;

import java.util.ArrayList;
import java.util.Collection;

import smartgov.core.agent.core.Agent;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.agent.actuator.CarMover;
import smartgov.urban.osm.agent.behavior.BasicBehavior;
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
	public Collection<Agent> buildAgents() {
		Collection<Agent> agents = new ArrayList<>();
		int agentCount = Integer.valueOf((String) getOsmContext().getConfig().get("AgentNumber"));
		for(int i = 0; i < agentCount; i++){
			
			OsmAgentBody body = createAgentBody(getOsmContext());
			
			OsmAgent newAgent = new OsmAgent(
					String.valueOf(i),
					body,
					new BasicBehavior(body, getOsmContext()));

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
	 * @param environment Current OsmEnvironment
	 * @return Created OsmAgentBody
	 */
	public OsmAgentBody createAgentBody(OsmContext environment) {
		return new OsmAgentBody(
				new CarMover(),
				environment);
	}
}
