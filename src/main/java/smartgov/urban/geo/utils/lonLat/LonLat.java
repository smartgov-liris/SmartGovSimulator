package smartgov.urban.geo.utils.lonLat;

import org.locationtech.jts.geom.Coordinate;

import smartgov.urban.geo.utils.LatLon;
import smartgov.urban.geo.utils.Projection;

public class LonLat implements Projection {

	@Override
	public Coordinate project(LatLon geoPoint) {
		return new Coordinate(geoPoint.lon, geoPoint.lat);
	}

	@Override
	public LatLon unproject(Coordinate point) {
		return new LatLon(point.y, point.x);
	}

}
