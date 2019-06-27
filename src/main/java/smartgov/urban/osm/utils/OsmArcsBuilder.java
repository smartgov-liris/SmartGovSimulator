package smartgov.urban.osm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;

public class OsmArcsBuilder {
	
	public static List<OsmArc> buildArcs(Map<String, OsmNode> nodes, List<Road> roads) {
		List<OsmArc> arcs = new ArrayList<>();
		int id = 1;
		
		for (Road road : roads) {
			for(int i = 0; i < road.getNodes().size() - 1; i++) {
				arcs.add(
						new OsmArc(
								String.valueOf(id),
								road,
								RoadDirection.FORWARD,
								nodes.get(road.getNodes().get(i)),
								nodes.get(road.getNodes().get(i+1))
								)
							);
				
			}
			if (!road.isOneway()) {
				for(int i = road.getNodes().size() - 1; i > 1; i--) {
					arcs.add(
							new OsmArc(
									String.valueOf(id),
									road,
									RoadDirection.BACKWARD,
									nodes.get(road.getNodes().get(i)),
									nodes.get(road.getNodes().get(i-1))
									)
								);
					
				}
			}
		}
		
		return arcs;
	}

}
