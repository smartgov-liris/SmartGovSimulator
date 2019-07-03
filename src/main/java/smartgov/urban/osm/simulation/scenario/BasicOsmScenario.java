package smartgov.urban.osm.simulation.scenario;

import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;

public abstract class BasicOsmScenario extends GenericOsmScenario<OsmNode, Road> {

	public BasicOsmScenario() {
		super(OsmNode.class, Road.class);
	}

}
