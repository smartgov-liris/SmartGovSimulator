package smartgov.urban.osm.environment;

import smartgov.core.environment.SmartGovContext;
import smartgov.core.simulation.Scenario;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.environment.graph.factory.DefaultOsmArcFactory;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SinkNode;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.SourceNode;
import smartgov.urban.osm.simulation.scenario.lowLayer.RandomTrafficScenario;
import smartgov.urban.osm.simulation.scenario.lowLayer.ScenarioVisualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CoreEnvironment extension that adds OSM specific features to the simulation environment.
 * 
 * @author pbreugnot
 *
 */
public class OsmContext extends SmartGovContext {
	
	public List<Road> roads;
	
	private Map<String, SourceNode> sourceNodes;
	private Map<String, SinkNode> sinkNodes;
	
	public OsmContext(String configFile) {
		super(configFile);
		roads = new ArrayList<>();
		sourceNodes = new HashMap<>();
		sinkNodes = new HashMap<>();
	}
	
	/**
	 * Source node available for random traffic agents.
	 *
	 * @return Source nodes map
	 */
	public Map<String, SourceNode> getSourceNodes() {
		return sourceNodes;
	}

	/**
	 * Sink nodes available for random traffic agents.
	 *
	 * @return Sink nodes map
	 */
	public Map<String, SinkNode> getSinkNodes() {
		return sinkNodes;
	}
	
	@Override
	public void clear(){
		super.clear();
		roads.clear();
		sourceNodes.clear();
		sinkNodes.clear();
	}
	
	/**
	 * Can load the following scenarios :
	 * <ul>
	 * 	<li> "visualization": Loads only the nodes and arcs</li>
	 * 	<li> "randomTraffic": Loads the OSM graph and create some
	 * 	random traffic agents </li>
	 * </ul>
	 */
	@Override
	public Scenario loadScenario(String scenarioName) {
		super.loadScenario(scenarioName);
		switch (scenarioName) {
			case ScenarioVisualization.name:
				return new ScenarioVisualization<OsmNode, Road>(OsmNode.class, Road.class, new DefaultOsmArcFactory());
			case RandomTrafficScenario.name:
				return new RandomTrafficScenario<OsmNode, Road>(OsmNode.class, Road.class, new DefaultOsmArcFactory());
			default:
				return null;
		}
	}
}
