package org.liris.smartgov.simulator.core.agent.moving.behavior;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MoverAction;
import org.liris.smartgov.simulator.core.agent.moving.behavior.MovingBehavior;
import org.liris.smartgov.simulator.core.agent.moving.events.node.DestinationReachedEvent;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.events.EventHandler;

public class TestMovingBehavior extends MovingBehavior {
	
	public TestMovingBehavior(MovingAgentBody agentBody, Node node1, Node node2, SmartGovContext context) {
		super(agentBody, node1, node2, context);
		registerDestinationReachedListener(agentBody);
	}

	private void registerDestinationReachedListener(MovingAgentBody agentBody) {
		agentBody.addOnDestinationReachedListener(new EventHandler<DestinationReachedEvent>() {

			@Override
			public void handle(DestinationReachedEvent event) {
				// On destination reached, invert origin and destination to do the return trip
				// The agentBody's plan will be updated accordingly to find the shortest path
				// from the new origin to the new destination.
				refresh(getDestination(), getOrigin());
			}
			
		});
	}
	
	@Override
	public MoverAction provideAction() {
		return MoverAction.MOVE();
	}

}
