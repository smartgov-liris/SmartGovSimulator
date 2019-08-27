package org.liris.smartgov.simulator.urban.osm.environment.graph.factory;

import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmNode;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc.RoadDirection;

public class DefaultOsmArcFactory implements OsmArcFactory<OsmArc> {

	@Override
	public OsmArc create(String id, OsmNode startNode, OsmNode targetNode, Road road, RoadDirection roadDirection) {
		return new OsmArc(id, startNode, targetNode, road, roadDirection);
	}

}
