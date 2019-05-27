package smartgov.urban.osm.environment.city;

import java.util.Map;

import org.locationtech.jts.geom.Coordinate;

/**
 * Home implementation of Building.
 * 
 * @see Building
 * @author pbreugnot
 *
 */
public class Home extends Building {
	
	public Home(
			String id,
			Map<String, String> attributes,
			Coordinate[] polygon){
		super(id, attributes, polygon);
	}
	
}
