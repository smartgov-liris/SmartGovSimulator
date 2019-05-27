package smartgov.urban.osm.agent.properties;

import java.util.Random;

import smartgov.core.agent.properties.AgentProperties;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.AbstractOsmSinkSourceNode;

/**
 * General purpose properties for agents.
 * @author Simon
 *
 */
public class OsmAgentProperties extends AgentProperties {

	private OsmContext context;
	private GeoNode<?> beginNode;
	private GeoNode<?> endNode;
	
	public OsmAgentProperties(OsmContext context) {
		this.context = context;
	}
	/**
	 * Called to re-initialize properties.
	 */
	@Override
	public void initialize() {
		Random rnd = new Random();
		
		// Set up new origin
		String randomSourceNodeId = (String) context.sourceNodes.keySet().toArray()[rnd.nextInt(context.sourceNodes.size())];
		beginNode = (AbstractOsmSinkSourceNode) context.sourceNodes.get(randomSourceNodeId);
		
		// Set up new destination
		String randomSinkNodeId = (String) context.sinkNodes.keySet().toArray()[rnd.nextInt(context.sinkNodes.size())];
		endNode = (AbstractOsmSinkSourceNode) context.sinkNodes.get(randomSinkNodeId);
	}

	public OsmAgentProperties(GeoNode<?> beginNode, GeoNode<?> endNode){
		this.beginNode = beginNode;
		this.endNode = endNode;
	}

	public GeoNode<?> getBeginNode() {
		return beginNode;
	}

	public GeoNode<?> getEndNode() {
		return endNode;
	}
	
	

}
