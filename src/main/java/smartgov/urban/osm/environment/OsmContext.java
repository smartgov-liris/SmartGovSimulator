package smartgov.urban.osm.environment;

import net.sf.javaml.core.kdtree.KDTree;
import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.simulation.Scenario;
import smartgov.urban.osm.agent.OsmAgent;
import smartgov.urban.osm.environment.city.Building;
import smartgov.urban.osm.environment.city.Home;
import smartgov.urban.osm.environment.city.ParkingSpot;
import smartgov.urban.osm.environment.city.WorkOffice;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SourceNode;
import smartgov.urban.osm.simulation.scenario.OsmScenario;
import smartgov.urban.osm.simulation.scenario.lowLayer.ScenarioLowAgents;
import smartgov.urban.osm.simulation.scenario.lowLayer.ScenarioVisualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.locationtech.jts.geom.GeometryFactory;

/**
 * CoreEnvironment extension that adds OSM specific features to the simulation environment.
 * 
 * @author pbreugnot
 *
 */
public class OsmContext extends SmartGovContext {
	
	//Scenario
	public static OsmScenario scenario;

	//Repast Static Variables
	public static final GeometryFactory GEOFACTORY = new GeometryFactory();
	// public static Parameters params;
	
	//Indicator's name
	public static final String SEARCHING_TIME = "Searching_Time";
	public static final String DISTANCE_FROM_WORK = "DistanceWork";
	public static final String OCCUPATION = "Occupation";
	public static final String PRICE = "Price";
	
	//Context elements
	public List<Building> buildings;
	public List<Home> homes;
	public List<WorkOffice> offices;
	public List<Road> roads;
	public List<ParkingSpot> parkingSpots;

	public List<OsmArc> edgesWithSpots;
	
	private Map<String, SourceNode> sourceNodes;
	private Map<String, SinkNode> sinkNodes;

	//Manage human agent creation and allocation
	public Map<String, Queue<Agent>> agentsStock; // Map SourceNodes ids to available agents
	
	//File names
	public static String occupationTestFile;
	
	//KDTree for spatial research
	public KDTree kdtreeArcsWithSpots;
	public KDTree kdtreeWithSpots;
	
	public OsmContext(String configFile) {
		super(configFile);
		// edgesOSM.clear();
		buildings = new ArrayList<>();
		homes = new ArrayList<>();
		offices = new ArrayList<>();
		roads = new ArrayList<>();
		parkingSpots = new ArrayList<>();
		edgesWithSpots = new ArrayList<>();
		sourceNodes = new HashMap<>();
		sinkNodes = new HashMap<>();
		agentsStock = new HashMap<>();	
		kdtreeArcsWithSpots = new KDTree(2);
		kdtreeWithSpots = new KDTree(2);
	}
	
	public Map<String, SourceNode> getSourceNodes() {
		return sourceNodes;
	}

	public Map<String, SinkNode> getSinkNodes() {
		return sinkNodes;
	}
	
	@Override
	public void clear(){
		super.clear();
		buildings.clear();
		homes.clear();
		offices.clear();
		roads.clear();
		parkingSpots.clear();
		edgesWithSpots.clear();
		sourceNodes.clear();
		sinkNodes.clear();
		kdtreeArcsWithSpots = new KDTree(2);
		kdtreeWithSpots = new KDTree(2);
		resetSpecialList();
	}
	
	protected void resetSpecialList(){
		for(Queue<?> queue : agentsStock.values()){
				queue.clear();
		}
		agentsStock.clear();
	}
	
	@Override
	public Scenario loadScenario(String scenarioName) {
		super.loadScenario(scenarioName);
		switch (scenarioName) {
			case ScenarioVisualization.name:
				return new ScenarioVisualization(this);
			case ScenarioLowAgents.name:
				return new ScenarioLowAgents(this);
//			case AgentListenersScenario.name:
//				return new AgentListenersScenario(this);
			default:
				// return new ScenarioSimonLA();
				return null;
		}
	}
}
