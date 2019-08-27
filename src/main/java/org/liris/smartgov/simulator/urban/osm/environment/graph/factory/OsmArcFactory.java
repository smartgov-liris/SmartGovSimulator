package org.liris.smartgov.simulator.urban.osm.environment.graph.factory;

import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmNode;
import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;
import org.liris.smartgov.simulator.urban.osm.environment.graph.OsmArc.RoadDirection;

public interface OsmArcFactory<T extends OsmArc> {

	public T create(
			String id,
			OsmNode startNode,
			OsmNode targetNode,
			Road road,
			RoadDirection roadDirection
			);
	
}
