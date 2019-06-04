package smartgov.urban.osm.agent.behavior;

import smartgov.core.agent.behavior.AbstractBehavior;
import smartgov.core.environment.LowLevelAction;
import smartgov.urban.osm.agent.OsmAgentBody;

public class BasicBehavior extends AbstractBehavior {

	public BasicBehavior(OsmAgentBody agentBody) {
		super(agentBody);
	}

	@Override
	public LowLevelAction provideAction() {
		return doAction();
	}
	
	private LowLevelAction doAction(){
		return LowLevelAction.MOVE;

	}
}
