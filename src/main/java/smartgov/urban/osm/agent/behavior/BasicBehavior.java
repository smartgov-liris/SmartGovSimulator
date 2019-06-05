package smartgov.urban.osm.agent.behavior;

import java.util.Random;

import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.node.Node;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.AbstractOsmSinkSourceNode;

public class BasicBehavior extends MovingBehavior {
	
	public BasicBehavior(OsmAgentBody agentBody, SmartGovContext context) {
		super(agentBody, null, null, context);
		Random rnd = new Random();
		origin = selectRandomSourceNode(rnd);
		destination = selectRandomSinkNode(rnd, origin);
	}
	
	public void refresh() {
		Random rnd = new Random();
		Node origin = selectRandomSourceNode(rnd);
		Node destination = selectRandomSinkNode(rnd, origin);
		refresh(origin, destination);
	}
	
	private AbstractOsmSinkSourceNode selectRandomSourceNode(Random rnd) {
		String randomSourceNodeId = (String) getContext().getSourceNodes().keySet().toArray()[rnd.nextInt(getContext().getSourceNodes().size())];
		return (AbstractOsmSinkSourceNode) getContext().getSourceNodes().get(randomSourceNodeId);
	}
	
	private AbstractOsmSinkSourceNode selectRandomSinkNode(Random rnd, Node sourceNode) {
		String randomSinkNodeId = (String) getContext().getSinkNodes().keySet().toArray()[rnd.nextInt(getContext().getSinkNodes().size())];
		while(randomSinkNodeId == sourceNode.getId()) {
			randomSinkNodeId = (String) getContext().getSinkNodes().keySet().toArray()[rnd.nextInt(getContext().getSinkNodes().size())];
		}
		return (AbstractOsmSinkSourceNode) getContext().getSinkNodes().get(randomSinkNodeId);
	}
	
	@Override
	public MoverAction provideAction() {
		return MoverAction.MOVE();
	}
}
