package org.liris.smartgov.simulator.core.environment.graph.astar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Graph;
import org.liris.smartgov.simulator.core.environment.graph.Node;

/**
 * The major part of this class has been directly copy/pasted from
 * the <a href="http://graphstream-project.org/">graphstrean</a>
 * <a href="https://github.com/graphstream/gs-algo/blob/master/src/org/graphstream/algorithm/AStar.java">
 * AStar source code</a>, distributed under a <a href="http://www.gnu.org/licenses/gpl-3.0.html">GPLv3</a>
 * license.
 * 
 * <p>
 * This has been done for efficiency reasons, to directly use the our graph structure
 * and avoid multiplying information with graph copies.
 * </p>
 * 
 *
 */
public class AStar {
	/**
	 * The graph.
	 */
	protected Graph graph;

	/**
	 * How to compute the path cost, the cost between two nodes and the
	 * heuristic. The heuristic to estimate the distance from the current
	 * position to the target.
	 */
	protected Costs costs;

	/**
	 * The open set.
	 */
	protected Map<String, AStarNode> open = new TreeMap<>();

	/**
	 * The closed set.
	 */
	protected Map<String, AStarNode> closed = new TreeMap<>();

	/**
	 * If found the shortest path is stored here.
	 */
	protected List<Node> result;

	/**
	 * Set to false if the algorithm ran, but did not found any path from the
	 * source to the target, or if the algorithm did not run yet.
	 */
	protected boolean pathFound = false;

	/**
	 * New A* algorithm.
	 */
	public AStar() {
	}

	/**
	 * New A* algorithm on a given graph.
	 * 
	 * @param graph
	 *            The graph where the algorithm will compute paths.
	 */
	public AStar(Graph graph) {
		init(graph);
	}

	/**
	 * Specify how various costs are computed. The costs object is in charge of
	 * computing the cost of displacement from one node to another (and
	 * therefore allows to compute the cost from the source node to any node).
	 * It also allows to compute the heuristic to use for evaluating the cost
	 * from the current position to the target node. Calling this DOES NOT clear
	 * the currently computed paths.
	 * 
	 * @param costs
	 *            The cost method to use.
	 */
	public void setCosts(Costs costs) {
		this.costs = costs;
	}

	private void init(Graph graph) {
		clearAll();
		this.graph = graph;
	}

	/**
	 * The computed path, or null if nor result was found.
	 * 
	 * @return The computed path, or null if no path was found.
	 */
	public List<Node> getShortestPath() {
		return result;
	}

	/**
	 * Build the shortest path from the target/destination node, following the
	 * parent links.
	 * 
	 * @param target
	 *            The destination node.
	 * @return The path.
	 */
	private List<Node> buildPath(AStarNode target) {
		List<Node> path = new ArrayList<>();

		ArrayList<AStarNode> thePath = new ArrayList<AStarNode>();
		AStarNode node = target;

		while (node != null) {
			thePath.add(node);
			node = node.parent;
		}
		
		for (int i = (thePath.size() - 1); i >= 0; i--) {
			path.add(thePath.get(i).node);
		}

		return path;
	}

	/**
	 * Compute the shortest path between the source and sink nodes.
	 * 
	 * @param source source node ID
	 * @param target target node ID
	 */
	public void compute(String source, String target) {
		Node sourceNode = graph.getNodes().get(source);
		Node targetNode = graph.getNodes().get(target);

		if (sourceNode == null)
			throw new RuntimeException("source node '" + source
					+ "' does not exist in the graph");

		if (targetNode == null)
			throw new RuntimeException("target node '" + target
					+ "' does not exist in the graph");

		aStar(sourceNode, targetNode);
	}

	/**
	 * Clear the already computed path.
	 */
	private void clearAll() {
		open.clear();
		closed.clear();

		result = null;
		pathFound = false;
	}

	/**
	 * The A* algorithm proper.
	 * 
	 * @param sourceNode
	 *            The source node.
	 * @param targetNode
	 *            The target node.
	 */
	private void aStar(Node sourceNode, Node targetNode) {
		clearAll();
		open.put(
				sourceNode.getId(),
				new AStarNode(sourceNode, null, null, 0, costs.heuristic(
						sourceNode, targetNode)));

		pathFound = false;

		while (!open.isEmpty()) {
			AStarNode current = getNextBetterNode();

			assert (current != null);

			if (current.node == targetNode) {
				// We found it !
				assert current.edge != null;
				pathFound = true;
				result = buildPath(current);
				return;
			} else {
				open.remove(current.node.getId());
				closed.put(current.node.getId(), current);

				// For each successor of the current node :

				Iterator<? extends Arc> nexts = current.node.getOutgoingArcs().iterator();

				while (nexts.hasNext()) {
					Arc edge = nexts.next();
					Node next = edge.getTargetNode();
					double h = costs.heuristic(next, targetNode);
					double g = current.g + costs.cost(edge);
					double f = g + h;

					// If the node is already in open with a better rank, we
					// skip it.

					AStarNode alreadyInOpen = open.get(next.getId());

					if (alreadyInOpen != null && alreadyInOpen.rank <= f)
						continue;

					// If the node is already in closed with a better rank; we
					// skip it.

					AStarNode alreadyInClosed = closed.get(next.getId());

					if (alreadyInClosed != null && alreadyInClosed.rank <= f)
						continue;

					closed.remove(next.getId());
					open.put(next.getId(), new AStarNode(next, edge, current, g, h));
				}
			}
		}
	}

	/**
	 * Find the node with the lowest rank in the open list.
	 * 
	 * @return The node of open that has the lowest rank.
	 */
	protected AStarNode getNextBetterNode() {
		// TODO: consider using a priority queue here ?
		// The problem is that we use open has a hash to ensure
		// a node we will add to to open is not yet in it.

		double min = Float.MAX_VALUE;
		AStarNode theChosenOne = null;

		for (AStarNode node : open.values()) {
			if (node.rank < min) {
				theChosenOne = node;
				min = node.rank;
			}
		}

		return theChosenOne;
	}
	
	/**
	 * Representation of a node in the A* algorithm.
	 * 
	 * <p>
	 * This representation contains :
	 * <ul>
	 * <li>the node itself;</li>
	 * <li>its parent node (to reconstruct the path);</li>
	 * <li>the g value (cost from the source to this node);</li>
	 * <li>the h value (estimated cost from this node to the target);</li>
	 * <li>the f value or rank, the sum of g and h.</li>
	 * </ul>
	 * </p>
	 */
	private class AStarNode {
		/**
		 * The node.
		 */
		public Node node;

		/**
		 * The node's parent.
		 */
		public AStarNode parent;

		/**
		 * The edge used to go from parent to node.
		 */
		public Arc edge;

		/**
		 * Cost from the source node to this one.
		 */
		public double g;

		/**
		 * Estimated cost from this node to the destination.
		 */
		public double h;

		/**
		 * Sum of g and h.
		 */
		public double rank;

		/**
		 * New A* node.
		 * 
		 * @param node
		 *            The node.
		 * @param edge
		 *            The edge used to go from parent to node.
		 * @param parent
		 *            It's parent node.
		 * @param g
		 *            The cost from the source to this node.
		 * @param h
		 *            The estimated cost from this node to the target.
		 */
		public AStarNode(Node node, Arc edge, AStarNode parent, double g,
				double h) {
			this.node = node;
			this.edge = edge;
			this.parent = parent;
			this.g = g;
			this.h = h;
			this.rank = g + h;
		}
}
}
