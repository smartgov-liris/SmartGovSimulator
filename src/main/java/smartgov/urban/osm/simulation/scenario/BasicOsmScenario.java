package smartgov.urban.osm.simulation.scenario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.simulation.Scenario;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.utils.OsmArcsBuilder;
import smartgov.urban.osm.utils.OsmLoader;

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
public abstract class BasicOsmScenario extends Scenario {
	
	@Override
	public Collection<? extends Node> buildNodes(SmartGovContext context) {
		OsmLoader<OsmNode> loader = new OsmLoader<>();
		
		try {
			return loader.loadOsmElements(
					context.getFileLoader().load("nodes"),
					OsmNode.class
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public Collection<? extends Arc> buildArcs(SmartGovContext context) {
		OsmLoader<Road> loader = new OsmLoader<>();
		
		Collection<OsmArc> arcs = new ArrayList<>();
		try {
			List<Road> roads = loader.loadOsmElements(
					context.getFileLoader().load("roads"),
					Road.class
					);
			arcs = OsmArcsBuilder.buildArcs(context.nodes, roads);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arcs;
	}
	
}
