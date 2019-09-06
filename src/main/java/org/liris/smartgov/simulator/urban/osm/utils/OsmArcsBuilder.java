package org.liris.smartgov.simulator.urban.osm.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.urban.osm.environment.OsmContext;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmNode;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc.RoadDirection;
import org.liris.smartgov.simulator.urban.osm.environment.graph.factory.OsmArcFactory;

public class OsmArcsBuilder {
	
	/**
	 * Build a list of arc from a set of roads, using the specified nodes
	 * and the arc factory.
	 * 
	 * <p>
	 * For roads that are not <i>oneway</i> (as the major part of osm roads),
	 * two arcs are created between each road nodes, for each direction.
	 * </p>
	 * 
	 * @param nodes available nodes
	 * @param roads roads set
	 * @param arcFactory arc builder
	 * @return list of generated osm arcs
	 */
	public static List<OsmArc> buildArcs(
			Map<String, ? extends Node> nodes,
			Collection<Road> roads,
			OsmArcFactory<? extends OsmArc> arcFactory) {
		int currentId = 0;
		
		List<OsmArc> arcs = new ArrayList<>();
		
		for (Road road : roads) {
			// TODO : This should be handled by the OSM parser.
			clearRoad(road, nodes.keySet());
			for(int i = 0; i < road.getNodes().size() - 1; i++) {
				OsmArc newArc = arcFactory.create(
						String.valueOf(currentId),
						(OsmNode) nodes.get(road.getNodes().get(i)),
						(OsmNode) nodes.get(road.getNodes().get(i+1)),
						road,
						RoadDirection.FORWARD
						);
				arcs.add(newArc);
				road.addArc(newArc);

				((OsmNode) nodes.get(road.getNodes().get(i))).setRoad(road);
				currentId++;
			}
			((OsmNode) nodes.get(road.getNodes().get(road.getNodes().size() - 1))).setRoad(road);

			if (!road.isOneway()) {
				for(int i = road.getNodes().size() - 1; i > 0; i--) {
					OsmArc newArc = arcFactory.create(
							String.valueOf(currentId),
							(OsmNode) nodes.get(road.getNodes().get(i)),
							(OsmNode) nodes.get(road.getNodes().get(i-1)),
							road,
							RoadDirection.BACKWARD
							);
					arcs.add(newArc);
					road.addArc(newArc);
					currentId++;
				}
			}
			road.checkIntegrity();
		}
		
		return arcs;
	}
	
	/**
	 * Fixes dead ends eventually contained in the OSM graph contained in the input context,
	 * adding arcs in the opposite direction even if the road was originally a oneway road.
	 * Such situations are actually impossible in real life : a dead end can't be oneway,
	 * otherwise cars would be indefinitely stuck in it. Either it's not a real dead end
	 * (there is an "out" path), or it is not really oneway.
	 * 
	 * <p>
	 * The algorithm only fixes the following situations, where 4 is considered as a dead end,
	 * and nodes 1 and 2 can be connected to any other nodes. Node 3 is optional, but used for
	 * illustration purposes.
	 * <ul>
	 * <li> 1 &#60;=&#62; 2 =&#62; 3 =&#62; 4 </li>
	 * <li> 1 &#60;=&#62; 2 &#60;= 3 &#60;= 4 </li>
	 * </ul>
	 * The algorithm will replace those two situations as follow :
	 * <ul>
	 * <li> 1 &#60;=&#62; 2 &#60;=&#62; 3 &#60;=&#62; 4 </li>
	 * </ul>
	 * 
	 * Notice that the following situation <b>is not handled</b> :
	 * <ul>
	 * <li> 1 &#60;=&#62; 2 =&#62; 3 &#60;=&#62; 4 </li>
	 * </ul>
	 * In that case, 3 and 4 might also look as dead ends, because any agent reaching
	 * one of those two nodes will be stuck, but solving such situations would require some
	 * directed graph connectivity check and fixing, what is far from this specific and simple solution.
	 * 
	 * @param context context to which new arcs will be added if necessary
	 * @param arcFactory arc factory used to build additional arcs
	 */
	public static void fixDeadEnds(OsmContext context, OsmArcFactory<? extends OsmArc> arcFactory) {
		for(Node node : context.nodes.values()) {
			/*
			 * First case : 1 <=> 2 => 3 => 4
			 */
			if(node.getOutgoingArcs().size() == 0) {
				if(!node.getIncomingArcs().isEmpty()) { // This should obviously always be true in this subcase, but if its not the case we don't care
					OsmNode deadEnd = (OsmNode) node;
					OsmArc currentArc = (OsmArc) node.getIncomingArcs().get(0);
					OsmNode firstDeadEndNode = (OsmNode) currentArc.getStartNode();
					while(firstDeadEndNode.getOutgoingArcs().size() == 1 && firstDeadEndNode.getIncomingArcs().size() >= 1) {
						Node tempNode = firstDeadEndNode;
						firstDeadEndNode = (OsmNode) currentArc.getStartNode();
						currentArc = (OsmArc) tempNode.getIncomingArcs().get(0);
					}
					OsmNode currentNode = deadEnd;
					while(!currentNode.equals(firstDeadEndNode)) {
						OsmArc deadArc = (OsmArc) currentNode.getIncomingArcs().get(0);
						OsmNode previousNode = (OsmNode) deadArc.getStartNode();
						RoadDirection oppositeDirection;
						switch(deadArc.getRoadDirection()) {
						case FORWARD:
							oppositeDirection = RoadDirection.BACKWARD;
							break;
						default:
							oppositeDirection = RoadDirection.FORWARD;
							break;
						}
						OsmArc newArc = arcFactory.create(
								deadArc.getId() + "bis",
								currentNode,
								previousNode,
								deadArc.getRoad(),
								oppositeDirection);
						context.arcs.put(newArc.getId(), newArc);
						currentNode = previousNode;
					}
					deadEnd.getRoad().setOneway(false);
				}
			}
			/*
			 * Second case : 1 <=> 2 <= 3 <= 4
			 * 4 is considered as a dead end, because it can't be reached.
			 */
			if(node.getIncomingArcs().isEmpty()) {
				if(!node.getOutgoingArcs().isEmpty()) {
					OsmNode deadEnd = (OsmNode) node;
					OsmArc currentArc = (OsmArc) node.getOutgoingArcs().get(0);
					OsmNode firstDeadEndNode = (OsmNode) currentArc.getTargetNode();
					while(firstDeadEndNode.getIncomingArcs().size() == 1 && firstDeadEndNode.getOutgoingArcs().size() >= 1) {
						Node tempNode = firstDeadEndNode;
						firstDeadEndNode = (OsmNode) currentArc.getTargetNode();
						currentArc = (OsmArc) tempNode.getOutgoingArcs().get(0);
					}
					OsmNode currentNode = deadEnd;
					while(!currentNode.equals(firstDeadEndNode)) {
						OsmArc deadArc = (OsmArc) currentNode.getOutgoingArcs().get(0);
						OsmNode previousNode = (OsmNode) deadArc.getTargetNode();
						RoadDirection oppositeDirection;
						switch(deadArc.getRoadDirection()) {
						case FORWARD:
							oppositeDirection = RoadDirection.BACKWARD;
							break;
						default:
							oppositeDirection = RoadDirection.FORWARD;
							break;
						}
						OsmArc newArc = arcFactory.create(
								String.valueOf(deadArc.getId() + "bis"),
								previousNode,
								currentNode,
								deadArc.getRoad(),
								oppositeDirection);
						context.arcs.put(newArc.getId(), newArc);
						currentNode = previousNode;
					}
					deadEnd.getRoad().setOneway(false);
				}
			}
		}
	}
	
	private static void clearRoad(Road road, Collection<String> availableNodes) {
		Collection<String> nodesToRemove = new ArrayList<>();
		
		for(String node : road.getNodes()) {
			if(!availableNodes.contains(node)) {
				nodesToRemove.add(node);
			}
		}
		
		road.getNodes().removeAll(nodesToRemove);
	}

}
