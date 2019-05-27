package smartgov.core.simulation;

import java.util.Collection;

import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;

public abstract class Scenario {

	/**
	 * Add elements to context and instantiates agents.
	 * @param context Repast Simphony context.
	 * @return Updated context used by SmartGov simulator
	 */
	public void loadWorld(SmartGovContext context) {
		for (Node<?> node : buildNodes()) {
			context.nodes.put(node.getId(), node);
		}
		for (Arc<?> arc : buildArcs()) {
			context.arcs.put(arc.getId(), arc);
		}
	}
	
	public abstract Collection<Node<?>> buildNodes();
	public abstract Collection<Arc<?>> buildArcs();
}
