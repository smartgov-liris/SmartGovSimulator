package smartgov.urban.osm.environment.city;

import java.util.Map;

import org.locationtech.jts.geom.Coordinate;

/**
 * Default Building implementation, in case no other BuildingType matches.
 * 
 * @author pbreugnot
 *
 */
public class DefaultBuilding extends Building {

	public DefaultBuilding(
			String id,
			Map<String, String> attributes,
			Coordinate[] polygon) {
		super(id, attributes, polygon);
	}

}
