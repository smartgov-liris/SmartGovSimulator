package smartgov.urban.osm.agent.behavior;

import smartgov.core.agent.behavior.AbstractBehavior;
import smartgov.core.environment.LowLevelAction;
import smartgov.urban.osm.agent.OsmAgentBody;

public class BasicBehavior extends AbstractBehavior<OsmAgentBody>{

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

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}
