package smartgov.urban.osm.simulation.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import smartgov.SmartGov;
import smartgov.core.environment.graph.OrientedGraph;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;
import smartgov.core.simulation.Scenario;
import smartgov.urban.geo.environment.graph.GeoGraph;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.city.Building;
import smartgov.urban.osm.environment.city.Home;
import smartgov.urban.osm.environment.city.WorkOffice;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmGraph;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSinkSourceNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.OsmSourceNode;
import smartgov.urban.osm.simulation.parser.OsmJSONReader;

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
	protected OsmJSONReader jsonReader;

	public OsmScenario(OsmContext environment) {
		this.environment = environment;
		jsonReader = new OsmJSONReader();
		environment.init();
		loadOsmFeatures();
		createGraph();
		createOrientedGraph();
	}

	/**
	 * NodeFile, EdgeFile and RoadFile are required in order to have the minimum
	 * files to create an environment.
	 * BuildingFile and BuildingNodeFile are optional.
	 * @param geography
	 */
	private void loadOsmFeatures() {
		String nodeFile            = 	environment.getFiles().getFile("nodes_for_roads");
		String edgeFile            = 	environment.getFiles().getFile("edges");
		String roadFile            = 	environment.getFiles().getFile("roads");
		String buildingFile        =  	environment.getFiles().getFile("buildings");
		String buildingNodeFile    = 	environment.getFiles().getFile("nodes_for_buildings");
		String parkingFile    	   = 	environment.getFiles().getFile("parkings");

		long beginTime = System.currentTimeMillis();

		// TODO : This is temporary dirty solution. Should be refactored.

		// osmNodes ans osmArcs are not added to the environment.
		// They will be when buildNodes and buildArcs will be called,
		// and it should be the same for every OSM elements.

		SmartGov.logger.info("Loading nodes from " + nodeFile);
		osmNodes = jsonReader.parseNodeFile(nodeFile);

		SmartGov.logger.info("Loading buildings from " + buildingFile + ", " + buildingNodeFile);
		environment.buildings = jsonReader.parseBuildingFile(buildingFile, buildingNodeFile);
		addHomesAndWorkOfficesToContext();

		SmartGov.logger.info("Loading roads from " + roadFile);
		environment.roads = jsonReader.parseRoadFile(roadFile, osmNodes);

		SmartGov.logger.info("Loading arcs from " + edgeFile);
		osmArcs = jsonReader.readArcs(edgeFile, osmNodes, environment.roads);

		SmartGov.logger.info("Loading parking spots from " + parkingFile);
		environment.parkingSpots.addAll(jsonReader.readParkingWithoutBlockFaces(parkingFile, environment));

		SmartGov.logger.info("Generating Source and Sink Nodes");
		generateSourceAndSinkNodes();
		SmartGov.logger.info("Time to process 'parseOSMFiles': " + (System.currentTimeMillis() - beginTime) + "ms.");
	}

//	public Context<Object> initContext(Context<Object> context){
//		loadOsmFeatures();
//		populateContext(context, geography, elementsToAddToContext());
//		environment.init(); // Should be called AFTER loadFeatures()?
//		return context;
//	}
//
//	protected void parseConfigFile() {
//		environment.configFile = OsmEnvironment.parseConfig("input" + File.separator + "config.ini");
//	}

	private GeoGraph<OsmNode, OsmArc> createGraph() {
		OsmGraph roadGraph = new OsmGraph(environment, osmNodes, osmArcs);
		roadGraph.addParkingToRoad(environment.parkingSpots, osmArcs.values());
		return roadGraph;
	}

	private void createOrientedGraph() {
		long beginTime = System.currentTimeMillis();
		OrientedGraph<OsmNode, OsmArc> orientedGraph = new OrientedGraph<OsmNode, OsmArc>(osmNodes);
		SmartGov.logger.info("Time to create an oriented graph: " + (System.currentTimeMillis() - beginTime) + "ms.");
		beginTime = System.currentTimeMillis();
		environment.graph = orientedGraph;
		SmartGov.logger.info("Time to instanciate oriented graph singleton: " + (System.currentTimeMillis() - beginTime) + "ms.");
	}

	@Override
	public Collection<Node<?>> buildNodes() {
		Collection<Node<?>> nodes = new ArrayList<>();
		for(Node<?> node : osmNodes.values()) {
			// This is why I hate Java and its strong typing.
			nodes.add(node);
		}
		return nodes;
	}

	@Override
	public Collection<Arc<?>> buildArcs() {
		Collection<Arc<?>> arcs = new ArrayList<>();
		for(Arc<?> arc : osmArcs.values()) {
			// This is again why I hate Java and its strong typing.
			arcs.add(arc);
		}
		return arcs;
	}
	protected abstract void createAgents();

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
				environment.sinkNodes.put(sinkNode.getId(), sinkNode);
				environment.nodes.put(sinkNode.getId(), sinkNode); //Overrides the normal OsmNode entry
			}
			else if (node.getIncomingArcs().size() == 0 && node.getOutgoingArcs().size() > 0) {
				OsmSourceNode sourceNode = new OsmSourceNode(
						environment,
						node.getId(),
						node.getPosition(),
						node.getOutgoingArcs()
						);
				environment.sourceNodes.put(sourceNode.getId(), sourceNode);
				environment.nodes.put(sourceNode.getId(), sourceNode); //Overrides the normal OsmNode entry
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
					environment.sinkNodes.put(sinkSourceNode.getId(), sinkSourceNode);
					environment.sourceNodes.put(sinkSourceNode.getId(), sinkSourceNode);
					environment.nodes.put(sinkSourceNode.getId(), sinkSourceNode); //Overrides the normal OsmNode entry
				}
			}
		}
		SmartGov.logger.info("Number of source nodes : " + environment.sourceNodes.size());
		SmartGov.logger.info("Number of sink nodes : " + environment.sinkNodes.size());
	}
}
