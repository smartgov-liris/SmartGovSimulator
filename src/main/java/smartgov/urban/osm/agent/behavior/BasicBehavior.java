package smartgov.urban.osm.agent.behavior;

import java.util.Random;

import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.node.Node;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.AbstractOsmSinkSourceNode;

public class BasicBehavior extends MovingBehavior {

	private SmartGovContext context;
	
	public BasicBehavior(OsmAgentBody agentBody, SmartGovContext context) {
		super(agentBody, null, null);
		this.context = context;
		refresh();
	}
	
	public void refresh() {
		Random rnd = new Random();
		Node origin = selectRandomSourceNode(rnd);
		Node destination = selectRandomSinkNode(rnd, origin);
		refresh(origin, destination);
	}
	
	private AbstractOsmSinkSourceNode selectRandomSourceNode(Random rnd) {
		String randomSourceNodeId = (String) context.getSourceNodes().keySet().toArray()[rnd.nextInt(context.getSourceNodes().size())];
		return (AbstractOsmSinkSourceNode) context.getSourceNodes().get(randomSourceNodeId);
	}
	
	private AbstractOsmSinkSourceNode selectRandomSinkNode(Random rnd, Node sourceNode) {
		String randomSinkNodeId = (String) context.getSinkNodes().keySet().toArray()[rnd.nextInt(context.getSinkNodes().size())];
		while(randomSinkNodeId == sourceNode.getId()) {
			randomSinkNodeId = (String) context.getSinkNodes().keySet().toArray()[rnd.nextInt(context.getSinkNodes().size())];
		}
		return (AbstractOsmSinkSourceNode) context.getSinkNodes().get(randomSinkNodeId);
	}
	
	@Override
	public MoverAction provideAction() {
		return MoverAction.MOVE();
	}
}
