package org.liris.smartgov.simulator.urban.osm.scenario;

import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmNode;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;
import org.liris.smartgov.simulator.urban.osm.environment.graph.factory.DefaultOsmArcFactory;

public abstract class BasicOsmScenario extends GenericOsmScenario<OsmNode, Road> {

	public BasicOsmScenario() {
		super(OsmNode.class, Road.class, new DefaultOsmArcFactory());
	}

}
