package smartgov.urban.osm.environment;

import net.sf.javaml.core.kdtree.KDTree;
import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Node;
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
import smartgov.urban.osm.simulation.scenario.lowLayer.RandomTrafficScenario;
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
	
	public List<Road> roads;
	
	private Map<String, Node> sourceNodes;
	private Map<String, Node> sinkNodes;

	//Manage human agent creation and allocation
	public Map<String, Queue<Agent>> agentsStock; // Map SourceNodes ids to available agents
	
	public OsmContext(String configFile) {
		super(configFile);
		roads = new ArrayList<>();
		sourceNodes = new HashMap<>();
		sinkNodes = new HashMap<>();
		agentsStock = new HashMap<>();
	}
	
	public Map<String, Node> getSourceNodes() {
		return sourceNodes;
	}

	public Map<String, Node> getSinkNodes() {
		return sinkNodes;
	}
	
	@Override
	public void clear(){
		super.clear();
		roads.clear();
		sourceNodes.clear();
		sinkNodes.clear();
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
				return new ScenarioVisualization();
			case RandomTrafficScenario.name:
				return new RandomTrafficScenario();
//			case AgentListenersScenario.name:
//				return new AgentListenersScenario(this);
			default:
				// return new ScenarioSimonLA();
				return null;
		}
	}
}
