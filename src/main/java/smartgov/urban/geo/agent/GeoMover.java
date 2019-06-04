package smartgov.urban.geo.agent;

import org.locationtech.jts.geom.Coordinate;

public interface GeoMover {
	
	public Coordinate moveOn(double timeToMove);
	
}
