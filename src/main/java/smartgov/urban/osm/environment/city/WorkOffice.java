package smartgov.urban.osm.environment.city;

import java.util.ArrayList;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;

import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;
/**
 * WorkOffice implementation of Building.
 * 
 * @author pbreugnot
 *
 */
public class WorkOffice extends Building {
	
	public WorkOffice(
			String id,
			Map<String, String> attributes, 
			Coordinate[] polygon){
		super(id, attributes, polygon);
		closestNodesWithSpots = new ArrayList<GeoNode>();
	}
	
}
