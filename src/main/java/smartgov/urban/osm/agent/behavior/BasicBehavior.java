package smartgov.urban.osm.agent.behavior;

import java.util.Random;

import smartgov.core.agent.moving.behavior.MoverAction;
import smartgov.core.agent.moving.behavior.MovingBehavior;
import smartgov.core.environment.graph.Node;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.AbstractOsmSinkSourceNode;

public class BasicBehavior extends MovingBehavior {
	
	public BasicBehavior(OsmAgentBody agentBody, AbstractOsmSinkSourceNode origin, AbstractOsmSinkSourceNode destination, OsmContext context) {
		super(agentBody, origin, destination, context);
//		Random rnd = new Random();
//		origin = selectRandomSourceNode(rnd);
//		destination = selectRandomSinkNode(rnd, origin);
	}
	
	public void refresh() {
		Random rnd = new Random();
		Node origin = selectRandomSourceNode(rnd,(OsmContext) getContext());
		Node destination = selectRandomSinkNode(rnd, origin,(OsmContext) getContext());
		refresh(origin, destination);
	}
	
	public static AbstractOsmSinkSourceNode selectRandomSourceNode(Random rnd, OsmContext context) {
		String randomSourceNodeId = (String) context.getSourceNodes().keySet().toArray()[rnd.nextInt(context.getSourceNodes().size())];
		return (AbstractOsmSinkSourceNode) context.getSourceNodes().get(randomSourceNodeId);
	}
	
	public static AbstractOsmSinkSourceNode selectRandomSinkNode(Random rnd, Node sourceNode, OsmContext context) {
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
