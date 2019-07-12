package smartgov.urban.osm.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import smartgov.core.environment.graph.Node;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.environment.graph.factory.OsmArcFactory;

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
		List<OsmArc> arcs = new ArrayList<>();
		int id = 1;
		
		for (Road road : roads) {
			// TODO : This should be handled by the OSM parser.
			clearRoad(road, nodes.keySet());
			for(int i = 0; i < road.getNodes().size() - 1; i++) {
				OsmArc newArc = arcFactory.create(
						String.valueOf(id),
						(OsmNode) nodes.get(road.getNodes().get(i)),
						(OsmNode) nodes.get(road.getNodes().get(i+1)),
						road,
						RoadDirection.FORWARD
						);
				arcs.add(newArc);
				road.addArc(newArc);

				((OsmNode) nodes.get(road.getNodes().get(i))).setRoad(road);
				id++;
			}
			((OsmNode) nodes.get(road.getNodes().get(road.getNodes().size() - 1))).setRoad(road);

			if (!road.isOneway()) {
				for(int i = road.getNodes().size() - 1; i > 0; i--) {
					OsmArc newArc = arcFactory.create(
							String.valueOf(id),
							(OsmNode) nodes.get(road.getNodes().get(i)),
							(OsmNode) nodes.get(road.getNodes().get(i-1)),
							road,
							RoadDirection.BACKWARD
							);
					arcs.add(newArc);
					road.addArc(newArc);
					id++;
				}
			}
			road.checkIntegrity();
		}
		
		return arcs;
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
