package smartgov.urban.osm.agent.behavior;

import smartgov.core.agent.behavior.MoverAction;
import smartgov.core.agent.behavior.MovingBehavior;
import smartgov.urban.osm.agent.OsmAgentBody;

public class BasicBehavior extends MovingBehavior {

	public BasicBehavior(OsmAgentBody agentBody) {
		super(agentBody);
	}

	@Override
	public MoverAction provideAction() {
		return doAction();
	}
	
	private MoverAction doAction(){
		return MoverAction.MOVE();

	}
}
