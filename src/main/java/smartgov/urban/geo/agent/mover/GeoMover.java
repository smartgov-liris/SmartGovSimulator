package smartgov.urban.geo.agent.mover;

import org.locationtech.jts.geom.Coordinate;

/**
 * Mover interface for geographical environments.
 */
public interface GeoMover {

	// TODO : huge parameter ambiguity. Need to refactor
	// and unit test that.	
	public Coordinate moveOn(double distance);
	
}
