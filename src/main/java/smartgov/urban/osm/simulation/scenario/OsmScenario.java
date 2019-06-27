package smartgov.urban.osm.simulation.scenario;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import smartgov.SmartGov;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.simulation.Scenario;
import smartgov.urban.geo.environment.graph.GeoGraph;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.city.Building;
import smartgov.urban.osm.environment.city.Home;
import smartgov.urban.osm.environment.city.WorkOffice;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkSourceNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSourceNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SourceNode;

/**
 * Abstract base for OSM scenarios. Define functions to load OSM features parsed by the
 * SmartGov OSMParser, and to add the to the Repast Simphony context.
 *
 * Such features include :
 *  - OsmNodes
 *  - OsmArcs
 *  - Buildings (Homes and WorkOffices)
 *  - Parking Spots
 *
 * @author Simon
 */
public abstract class OsmScenario extends Scenario {

	private Map<String, OsmNode> osmNodes;
	private Map<String, OsmArc> osmArcs;

	private OsmContext environment;

	public OsmScenario(OsmContext environment) {
		this.environment = environment;
	}

	public OsmContext getOsmContext() {
		return environment;
	}

//	/**
//	 * NodeFile, EdgeFile and RoadFile are required in order to have the minimum
//	 * files to create an environment.
//	 * BuildingFile and BuildingNodeFile are optional.
//	 * @param geography
//	 */
//	private void loadOsmFeatures() {
//		String nodeFile            = 	environment.getFiles().getFile("nodes_for_roads");
//		String edgeFile            = 	environment.getFiles().getFile("edges");
//		String roadFile            = 	environment.getFiles().getFile("roads");
//		String buildingFile        =  	environment.getFiles().getFile("buildings");
//		String buildingNodeFile    = 	environment.getFiles().getFile("nodes_for_buildings");
//		String parkingFile    	   = 	environment.getFiles().getFile("parkings");
//
//		long beginTime = System.currentTimeMillis();
//
//		// TODO : This is temporary dirty solution. Should be refactored.
//
//		// osmNodes ans osmArcs are not added to the environment.
//		// They will be when buildNodes and buildArcs will be called,
//		// and it should be the same for every OSM elements.
//
//		SmartGov.logger.info("Loading nodes from " + nodeFile);
//		osmNodes = jsonReader.parseNodeFile(nodeFile);
//
//		SmartGov.logger.info("Loading buildings from " + buildingFile + ", " + buildingNodeFile);
//		environment.buildings = jsonReader.parseBuildingFile(buildingFile, buildingNodeFile);
//		addHomesAndWorkOfficesToContext();
//
//		SmartGov.logger.info("Loading roads from " + roadFile);
//		environment.roads = jsonReader.parseRoadFile(roadFile, osmNodes);
//
//		SmartGov.logger.info("Loading arcs from " + edgeFile);
//		osmArcs = jsonReader.readArcs(edgeFile, osmNodes, environment.roads);
//
//		SmartGov.logger.info("Loading parking spots from " + parkingFile);
//		environment.parkingSpots.addAll(jsonReader.readParkingWithoutBlockFaces(parkingFile, environment));
//
//		SmartGov.logger.info("Generating Source and Sink Nodes");
//		generateSourceAndSinkNodes();
//		SmartGov.logger.info("Time to process 'parseOSMFiles': " + (System.currentTimeMillis() - beginTime) + "ms.");
//	}

	private GeoGraph createGraph() {
		GeoGraph roadGraph = new GeoGraph(osmNodes, osmArcs);
		// roadGraph.addParkingToRoad(environment.parkingSpots, osmArcs.values());
		return roadGraph;
	}

	@Override
	public Collection<OsmNode> buildNodes(SmartGovContext context) {
		// loadOsmFeatures();
		return osmNodes.values();
	}

	@Override
	public Collection<OsmArc> buildArcs(SmartGovContext context) {
		// TODO : What is the real purpose of this function?
		createGraph();
		return osmArcs.values();
	}

	/*
	 * TODO: Temporary dirty solution.
	 */
	private void addHomesAndWorkOfficesToContext() {
		for(Building building : environment.buildings) {
			if(building instanceof Home) {
				environment.homes.add((Home) building);
			}
			else if (building instanceof WorkOffice) {
				environment.offices.add((WorkOffice) building);
			}
		}
	}

	private void generateSourceAndSinkNodes() {
		for(OsmNode node : osmNodes.values()) {
			if(node.getIncomingArcs().size() > 0 && node.getOutgoingArcs().size() == 0) {
				OsmSinkNode sinkNode = new OsmSinkNode(
						environment,
						node.getId(),
						node.getPosition(),
						node.getIncomingArcs()
						);
				environment.getSinkNodes().put(sinkNode.getId(), sinkNode);
				osmNodes.put(sinkNode.getId(), sinkNode); //Overrides the normal OsmNode entry
			}
			else if (node.getIncomingArcs().size() == 0 && node.getOutgoingArcs().size() > 0) {
				OsmSourceNode sourceNode = new OsmSourceNode(
						environment,
						node.getId(),
						node.getPosition(),
						node.getOutgoingArcs()
						);
				environment.getSourceNodes().put(sourceNode.getId(), sourceNode);
				osmNodes.put(sourceNode.getId(), sourceNode); //Overrides the normal OsmNode entry
			}
			else if (node.getIncomingArcs().size() == 1 && node.getOutgoingArcs().size() == 1) {
//				boolean incomingEqualOutcoming = true;
//				for (OsmArc arc : node.getIncomingArcs()) {
//					if (!node.getOutgoingArcs().contains(arc)) {
//						incomingEqualOutcoming = false;
//						break;
//					}
//				}
//				if (incomingEqualOutcoming) {
				if(node.getIncomingArcs().get(0).getStartNode().getId() == node.getOutgoingArcs().get(0).getTargetNode().getId()) {
					OsmSinkSourceNode sinkSourceNode = new OsmSinkSourceNode(
							environment,
							node.getId(),
							node.getPosition(),
							node.getIncomingArcs(),
							node.getOutgoingArcs()
							);
					environment.getSinkNodes().put(sinkSourceNode.getId(), sinkSourceNode);
					environment.getSourceNodes().put(sinkSourceNode.getId(), sinkSourceNode);
					osmNodes.put(sinkSourceNode.getId(), sinkSourceNode); //Overrides the normal OsmNode entry
				}
			}
		}
		for(SourceNode sourceNode : environment.getSourceNodes().values()) {
			environment.agentsStock.put(sourceNode.getId(), new LinkedList<>());
		}
		SmartGov.logger.info("Number of source nodes : " + environment.getSourceNodes().size());
		SmartGov.logger.info("Number of sink nodes : " + environment.getSinkNodes().size());
	}

//	/**
//	 * This function behaves has an arc factory. It is called by the OSMparser to generate
//	 * an arc from the OSM data, that are represented by the arguments of this function.
//	 *
//	 * It can be overridden by sub-scenarios to generate other OsmArcs types, but without
//	 * altering the parsing functions.
//	 *
//	 * @param geography Current Geometry
//	 * @param id Arc id
//	 * @param road Road to which the Arc belongs to.
//	 * @param startNode Start Node
//	 * @param targetNode Target Node
//	 * @param distance Length of the Arc
//	 * @param polyLine Shape of the Arc
//	 * @param lanes Number of OSM lanes
//	 * @param type Arc type. (OSM 'highway' attribute)
//	 * @return Created OsmArc
//	 */
//	public abstract OsmArc createArc(
//			String id,
//			Road road,
//			OsmNode startNode,
//			OsmNode targetNode,
//			int lanes,
//			String type);
}
