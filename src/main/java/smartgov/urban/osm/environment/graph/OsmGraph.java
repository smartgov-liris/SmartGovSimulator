package smartgov.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import smartgov.SmartGov;
import smartgov.urban.geo.environment.graph.GeoGraph;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.city.ParkingSpot;

/**
 * GeoGraph implementation to represent the OSM road graph.
 *
 * @see OsmNode
 * @see OsmArc
 *
 * @author pbreugnot
 *
 */
public class OsmGraph extends GeoGraph<OsmNode, OsmArc> {

	private OsmContext environment;

	private final int NEIGHBOOR_NODE_NUMBER = 5; //Change when map is reduced, usually 10
	private final int NEIGHBOOR_NODE_NUMBER_EXTENDED = 6; //Change when map is reduced, usually 20

	public OsmGraph(OsmContext environment, Map<String, OsmNode> nodes, Map<String, OsmArc> arcs) {
		super(nodes, arcs);
		this.environment = environment;
	}

	public void addParkingToRoad(List<ParkingSpot> spots, Collection<OsmArc> edges){
		long beginTime = System.currentTimeMillis();

		for(ParkingSpot spot : spots){
			//Get closest node
			Object[] nearestNodesObjects = getNearestNodesFrom(spot.getPosition(), NEIGHBOOR_NODE_NUMBER);

			//Get closest edge
			ArrayList<OsmNode> nearestNodes = new ArrayList<>();
			for(Object obj : nearestNodesObjects)
				nearestNodes.add((OsmNode)obj);
			OsmArc edge = (OsmArc) findNearestArcFromEdgeSpotAroundNodes(spot, nearestNodes);


			if(edge!=null){
				OsmNode node;
				if(GISComputation.GPS2Meter(edge.getStartNode().getPosition(), spot.getPosition()) < GISComputation.GPS2Meter(edge.getTargetNode().getPosition(), spot.getPosition()))
					node = edge.getStartNode();
				else
					node = edge.getTargetNode();
			//	for(int k = 0; k < edges.size(); k++){
				//	if(edges.get(k) == edge){
						edge.addParking(spot);
						spot.setIdNode(node.getId());
						if(!environment.edgesWithSpots.contains(edge)){
							environment.edgesWithSpots.add(edge);
						}
				//	}
			//	}
			} else { //If parking doesn't have a node, try the next 20 closest points
				boolean find = false;
				Object[] nodesTab = getNearestNodesFrom(spot.getPosition(), NEIGHBOOR_NODE_NUMBER_EXTENDED);
				List<OsmNode> nodesNearest = new ArrayList<>();
				for(int j = 0; j < nodesTab.length; j++){
					OsmNode nodeTemp = (OsmNode) nodesTab[j];
					nodesNearest.add(nodeTemp);
					OsmArc edgeTemp = (OsmArc) findNearestArcFromEdgeSpotAroundNodes(spot, nodesNearest);
					if(edgeTemp != null){
						find = true;
						spot.setIdNode(nodeTemp.getId());
						for(OsmArc arc : edges){ // TODO : Why?
							if(arc == edgeTemp){
								((OsmArc)edgeTemp).addParking(spot);
								if(!environment.edgesWithSpots.contains(edgeTemp)){
									environment.edgesWithSpots.add(edgeTemp);
								}
							}
						}
						//EnvVar.correctSpots.add(spots.get(i));
						//spots.get(i).setRoadId(edgeTemp.getId());
						break;
					}
				}
				if(!find){
					spot.setFailed(true);
				}
			}
		}

		SmartGov.logger.info("Time to process 'addParkingToRoad': " + (System.currentTimeMillis() - beginTime) + "ms.");
	}
}
