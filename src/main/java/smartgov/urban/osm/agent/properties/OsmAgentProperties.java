package smartgov.urban.osm.agent.properties;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.agent.properties.AgentProperties;
import smartgov.core.output.node.NodeIdSerializer;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.sinkSourceNodes.AbstractOsmSinkSourceNode;

/**
 * General purpose properties for agents.
 * @author Simon
 *
 */
public class OsmAgentProperties extends AgentProperties {

	@JsonIgnore
	private OsmContext context;
	@JsonSerialize(using=NodeIdSerializer.class)
	private GeoNode<?> beginNode;
	@JsonSerialize(using=NodeIdSerializer.class)
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
		String randomSourceNodeId = (String) context.getSourceNodes().keySet().toArray()[rnd.nextInt(context.getSourceNodes().size())];
		beginNode = (AbstractOsmSinkSourceNode) context.getSourceNodes().get(randomSourceNodeId);
		
		// Set up new destination
		endNode = selectRandomSinkNode(rnd);
		while(beginNode.getId() == endNode.getId()) {
			// To be sure that destination is not departure.
			endNode = selectRandomSinkNode(rnd);
		}
	}
	
	private AbstractOsmSinkSourceNode selectRandomSinkNode(Random rnd) {
		String randomSinkNodeId = (String) context.getSinkNodes().keySet().toArray()[rnd.nextInt(context.getSinkNodes().size())];
		return (AbstractOsmSinkSourceNode) context.getSinkNodes().get(randomSinkNodeId);
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
