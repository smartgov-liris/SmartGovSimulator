package smartgov.core.environment.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import smartgov.core.agent.AbstractAgentBody;
import smartgov.core.environment.graph.node.Node;

/**
 * Stores a list of nodes and common attributes of these set of nodes.
 * @author Simon, pbreugnot
 *
 */
public class Path<Tnode extends Node, Tbody extends AbstractAgentBody> {
	
	private String id;
	protected Map<String, String> attributes;
	private List<Tnode> nodes;
	private ArrayList<Tbody> agentsOnPath;
	
	public Path(String id, Map<String, String> attributes, List<Tnode> nodes){
		this.id = id;
		this.attributes = attributes;
		this.nodes = nodes;
		this.agentsOnPath = new ArrayList<>();
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public String getId() {
		return id;
	}
	
	public void setNodes(List<Tnode> nodes) {
		this.nodes = nodes;
	}
	
	public List<Tnode> getNodes() {
		return nodes;
	}

	public ArrayList<Tbody> getAgentsOnPath() {
		return agentsOnPath;
	}

}
