package smartgov.urban.geo.agent;

import org.locationtech.jts.geom.Coordinate;

/**
 * Mover interface for geographical environments.
 */
public interface GeoMover {

	// TODO : huge paramater ambiguity. Need to refactor
	// and unit test that.	
	public Coordinate moveOn(double timeToMove);
	
}
