package smartgov.core.agent.moving.behavior;

import smartgov.core.agent.moving.TestMovingAgentBody;
import smartgov.core.agent.moving.events.DestinationReachedEvent;
import smartgov.core.environment.TestContext;
import smartgov.core.environment.graph.Node;
import smartgov.core.events.EventHandler;

public class TestMovingBehavior extends MovingBehavior {
	
	public TestMovingBehavior(TestMovingAgentBody agentBody, Node node1, Node node2, TestContext context) {
		super(agentBody, node1, node2, context);
		registerDestinationReachedListener(agentBody);
	}

	private void registerDestinationReachedListener(TestMovingAgentBody agentBody) {
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
