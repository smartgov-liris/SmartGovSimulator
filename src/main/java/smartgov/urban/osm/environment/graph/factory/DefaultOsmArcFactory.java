package smartgov.urban.osm.environment.graph.factory;

import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmArc.RoadDirection;
import smartgov.urban.osm.environment.graph.OsmNode;
import smartgov.urban.osm.environment.graph.Road;

public class DefaultOsmArcFactory implements OsmArcFactory<OsmArc> {

	@Override
	public OsmArc create(String id, OsmNode startNode, OsmNode targetNode, Road road, RoadDirection roadDirection) {
		return new OsmArc(id, startNode, targetNode, road, roadDirection);
	}

}
