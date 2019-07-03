package smartgov.urban.osm.simulation.scenario;

import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;
import smartgov.urban.osm.environment.graph.factory.DefaultOsmArcFactory;

public abstract class BasicOsmScenario extends GenericOsmScenario<OsmNode, Road> {

	public BasicOsmScenario() {
		super(OsmNode.class, Road.class, new DefaultOsmArcFactory());
	}

}
