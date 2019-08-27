package org.liris.smartgov.simulator.urban.geo.utils;

import org.locationtech.jts.geom.Coordinate;

public interface Projection {
	
	/**
	 * Mean earth radius used in the {@link #haversineDistance haversineDistance}, computed from the 
	 * WGS84 coordinate system, also used by Open Street Map. Notice that this system
	 * actually use an ellipsoid to represent the earth, and so this "mean" distance
	 * actually correspond to the average value obtained from semi-minor and semi-major
	 * axis defined in the system.
	 */
	public static final double MEAN_EARTH_RADIUS = 6371009;

	public Coordinate project(LatLon geoPoint);
	public LatLon unproject(Coordinate point);
	
	/**
	 * Computes the Haversine distance from the specified latitudes and longitudes, using
	 * the {@link #MEAN_EARTH_RADIUS} defined in this class.
	 * 
	 * @param point1 first geographical point
	 * @param point2 second geographical point
	 * @return distance in meter
	 */
	public static double haversineDistance(LatLon point1, LatLon point2) {
		//haversine formula https://en.wikipedia.org/wiki/Haversine_formula
			double _lat1 = Math.toRadians(point1.lat);
			double _lon1 = Math.toRadians(point1.lon);
			double _lat2 = Math.toRadians(point2.lat);
			double _lon2 = Math.toRadians(point2.lon);
			double a = Math.pow(Math.sin((_lat1 - _lat2)/2),2) + Math.cos(_lat1)*Math.cos(_lat2)*Math.pow(Math.sin((_lon1-_lon2)/2),2);
			double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
			double d = MEAN_EARTH_RADIUS * c;
			return d;
	}
}
