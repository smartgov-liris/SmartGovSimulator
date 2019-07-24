package smartgov.core.environment.graph.astar;

import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;

public interface Costs {
	
	public double heuristic(Node node, Node target);
	
	public double cost(Arc arc);
}
