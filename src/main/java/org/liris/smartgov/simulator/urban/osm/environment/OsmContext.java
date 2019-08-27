package org.liris.smartgov.simulator.urban.osm.environment;

import java.util.Map;
import java.util.TreeMap;

import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmNode;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;
import org.liris.smartgov.simulator.urban.osm.environment.graph.factory.DefaultOsmArcFactory;
import org.liris.smartgov.simulator.urban.osm.environment.graph.sinkSourceNodes.SinkNode;
import org.liris.smartgov.simulator.urban.osm.environment.graph.sinkSourceNodes.SourceNode;
import org.liris.smartgov.simulator.urban.osm.scenario.lowLayer.RandomTrafficScenario;
import org.liris.smartgov.simulator.urban.osm.scenario.lowLayer.ScenarioVisualization;

/**
 * CoreEnvironment extension that adds OSM specific features to the simulation environment.
 * 
 * @author pbreugnot
 *
 */
public class OsmContext extends SmartGovContext {
	
	public Map<String, Road> roads;
	
	private Map<String, SourceNode> sourceNodes;
	private Map<String, SinkNode> sinkNodes;
	
	public OsmContext(String configFile) {
		super(configFile);
		roads = new TreeMap<>();
		sourceNodes = new TreeMap<>();
		sinkNodes = new TreeMap<>();
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
	protected Scenario loadScenario(String scenarioName) {
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
