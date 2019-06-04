package smartgov.urban.osm.agent.behavior;

import smartgov.core.agent.behavior.AbstractBehavior;
import smartgov.core.agent.behavior.MoverAction;
import smartgov.urban.osm.agent.OsmAgentBody;

public class BasicBehavior extends AbstractBehavior {

	public BasicBehavior(OsmAgentBody agentBody) {
		super(agentBody);
	}

	@Override
	public MoverAction provideAction() {
		return doAction();
	}
	
	private MoverAction doAction(){
		return MoverAction.MOVE;

	}
}
