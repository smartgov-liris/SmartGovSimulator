package smartgov.urban.geo.utils;

import org.locationtech.jts.geom.Coordinate;

public interface Projection {

	public Coordinate project(LatLon geoPoint);
	public LatLon unproject(Coordinate point);
	
	public static double euclidianDistance(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	public static double haversineDistance(double lat1, double lat2, double lon1, double lon2) {
		//haversine formula https://en.wikipedia.org/wiki/Haversine_formula
			double earthRadius = 6371000;
			double _lat1 = Math.toRadians(lat1);
			double _lon1 = Math.toRadians(lon1);
			double _lat2 = Math.toRadians(lat2);
			double _lon2 = Math.toRadians(lon2);
			double a = Math.pow(Math.sin((_lat1 - _lat2)/2),2) + Math.cos(_lat1)*Math.cos(_lat2)*Math.pow(Math.sin((_lon1-_lon2)/2),2);
			double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
			double d = earthRadius*c;
			return d;
	}
}
