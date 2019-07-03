package smartgov.urban.osm.simulation.scenario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.simulation.Scenario;
import smartgov.urban.osm.environment.OsmContext;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.utils.OsmArcsBuilder;
import smartgov.urban.osm.utils.OsmLoader;

/**
 * Abstract base for OSM scenarios. Define functions to load OSM features parsed
 * from input files.
 *
 * <p>
 * Nodes and arcs will be loaded respectively from <i>nodes</i> and <i>roads</i> file entries
 * of the configuration file.
 * </p>
 * 
 * <p>
 * Corresponding files should be json files as given in output of the SmartGovOsmParser.
 * </p>
 *
 * @author Simon
 */
public abstract class BasicOsmScenario extends Scenario {
	
	/**
	 * Loads osm nodes from the <code>nodes</code> json file of the
	 * configuration.
	 *
	 * @param context current context
	 * @return collection of loaded <code>OsmNode</code>s
	 */
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
	
	/**
	 * Loads osm roads from the <code>roads</code> json file specified in the
	 * configuration, and generates arcs accordingly thanks to the {@link
	 * smartgov.urban.osm.utils.OsmArcsBuilder OsmArcBuilder}.
	 *
	 * <p>
	 * Loaded roads are added to the input {@link
	 * smartgov.urban.osm.environment.OsmContext OsmContext}.
	 * </p>
	 *
	 * @param context current context
	 * @return collection of generated <code>OsmArcs</code>s
	 */

	@Override
	public Collection<? extends Arc> buildArcs(SmartGovContext context) {
		OsmLoader<Road> loader = new OsmLoader<>();
		
		Collection<OsmArc> arcs = new ArrayList<>();
		try {
			((OsmContext) context).roads = loader.loadOsmElements(
					context.getFileLoader().load("roads"),
					Road.class
					);
			arcs = OsmArcsBuilder.buildArcs(context.nodes, ((OsmContext) context).roads);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arcs;
	}
	
}
