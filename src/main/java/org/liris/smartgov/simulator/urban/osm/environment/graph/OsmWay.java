package org.liris.smartgov.simulator.urban.osm.environment.graph;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Minimal representation of an <a
 * href="https://wiki.openstreetmap.org/wiki/Way">osm way</a>.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsmWay {

	private String id;
	private List<String> nodeRefs;
	
	/*
	 * For each node, stores the reference to the arc
	 * going out from the node represented by the key id,
	 * and so that necessarily goes to the next node in
	 * the same order as in node refs.
	 */
	protected List<OsmArc> forwardArcs;
	protected List<OsmArc> backwardArcs;

	/**
	 * JsonCreator used to load Roads from a Json file.
	 *
	 * <p>
	 * Can also be used to create roads manually.
	 * </p>
	 *
	 * @param id road id
	 * @param nodeRefs an ordered list of node ids
	 */
	@JsonCreator
	public OsmWay(
		@JsonProperty("id") String id,
		@JsonProperty("nodeRefs") List<String> nodeRefs) {
		this.id = id;
		this.nodeRefs = nodeRefs;
		this.forwardArcs = new ArrayList<>();
		this.backwardArcs = new ArrayList<>();
	}

	/**
	 * Way id
	 *
	 * @return way id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Ordered list of node ids.
	 *
	 * @return ordered list of node ids
	 */
	public List<String> getNodes() {
		return nodeRefs;
	}
	
	public void addArc(OsmArc arc) {
		switch(arc.getRoadDirection()) {
		case BACKWARD:
			this.backwardArcs.add(arc);
			break;
		case FORWARD:
			this.forwardArcs.add(arc);
			break;
		default:
			break;
		
		}
		
	}
}
