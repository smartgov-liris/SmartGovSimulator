package org.liris.smartgov.simulator.core.agent.moving;

import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MoverAction;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MovingBehavior;

/**
 * Simple Agent implementation that represents agent moving in the simulation.
 *
 * Such an agent will perform {@link org.liris.smartgov.simulator.core.agent.moving.behavior.MoverAction}s
 * thanks to a {@link MovingAgentBody} and a {@link org.liris.smartgov.simulator.core.agent.moving.behavior.MovingBehavior}.
 */
public class MovingAgent extends Agent<MoverAction>{

	/**
	 * MovingAgent constructor.
	 *
	 * Sets the Plan of the specified MovingAgentBody, and updates it with the 
	 * origin and destination specified in the MovingBehavior.
	 *
	 * @param id Agent id
	 * @param body Agent body
	 * @param behavior Moving behavior
	 */
	public MovingAgent(String id, MovingAgentBody body, MovingBehavior behavior) {
		super(id, body, behavior);
		behavior.updateAgentBodyPlan();
	}

}
