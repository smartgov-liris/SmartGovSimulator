package smartgov.core.environment.graph.astar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Graph;
import smartgov.core.environment.graph.Node;

public class AStar {
	/**
	 * The graph.
	 */
	protected Graph graph;

	/**
	 * The source node id.
	 */
	protected String source;

	/**
	 * The target node id.
	 */
	protected String target;

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
	 * New A* algorithm on the given graph.
	 * 
	 * @param graph
	 *            The graph where the algorithm will compute paths.
	 * @param src
	 *            The start node.
	 * @param trg
	 *            The destination node.
	 */
	public AStar(Graph graph, String src, String trg) {
		this(graph);
		setSource(src);
		setTarget(trg);
	}

	/**
	 * Change the source node. This clears the already computed path, but
	 * preserves the target node name.
	 * 
	 * @param nodeName
	 *            Identifier of the source node.
	 */
	public void setSource(String nodeName) {
		clearAll();
		source = nodeName;
	}

	/**
	 * Change the target node. This clears the already computed path, but
	 * preserves the source node name.
	 * 
	 * @param nodeName
	 *            Identifier of the target node.
	 */
	public void setTarget(String nodeName) {
		clearAll();
		target = nodeName;
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

	/*
	 * @see
	 * org.graphstream.algorithm.Algorithm#init(org.graphstream.graph.Graph)
	 */
	public void init(Graph graph) {
		clearAll();
		this.graph = graph;
	}

	/*
	 * @see org.graphstream.algorithm.Algorithm#compute()
	 */
	public void compute() {
		if (source != null && target != null) {
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
	 * After having called {@link #compute()} or
	 * {@link #compute(String, String)}, if the {@link #getShortestPath()}
	 * returns null, or this method return true, there is no path from the given
	 * source node to the given target node. In other words, the graph has
	 * several connected components. It also return true if the algorithm did
	 * not run.
	 * 
	 * @return True if there is no possible path from the source to the
	 *         destination or if the algorithm did not run.
	 */
	public boolean noPathFound() {
		return (! pathFound);
	}

	/**
	 * Build the shortest path from the target/destination node, following the
	 * parent links.
	 * 
	 * @param target
	 *            The destination node.
	 * @return The path.
	 */
	public List<Node> buildPath(AStarNode target) {
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

//		int n = thePath.size();
//
//		if (n > 1) {
//			AStarNode current = thePath.get(n - 1);
//			AStarNode follow = thePath.get(n - 2);
//
//			path.add(current.node);
//
//			current = follow;
//
//			for (int i = n - 3; i >= 0; i--) {
//				follow = thePath.get(i);
//				// path.add(follow.edge);
//				current = follow;
//			}
//		}

		return path;
	}

	/**
	 * Call {@link #compute()} after having called {@link #setSource(String)}
	 * and {@link #setTarget(String)}.
	 * 
	 * @param source
	 *            Identifier of the source node.
	 * @param target
	 *            Identifier of the target node.
	 */
	public void compute(String source, String target) {
		setSource(source);
		setTarget(target);
		compute();
	}

	/**
	 * Clear the already computed path. This does not clear the source node
	 * name, the target node name and the weight attribute name.
	 */
	protected void clearAll() {
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
	protected void aStar(Node sourceNode, Node targetNode) {
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
	protected class AStarNode {
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
		 *            The edge used to go from parent to node (useful for
		 *            multi-graphs).
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
