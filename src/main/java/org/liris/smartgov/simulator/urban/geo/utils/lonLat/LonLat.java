package org.liris.smartgov.simulator.urban.geo.utils.lonLat;

import org.liris.smartgov.simulator.urban.geo.utils.LatLon;
import org.liris.smartgov.simulator.urban.geo.utils.Projection;
import org.locationtech.jts.geom.Coordinate;

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
