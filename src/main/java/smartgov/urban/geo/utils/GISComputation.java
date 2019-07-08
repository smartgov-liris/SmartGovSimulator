package smartgov.urban.geo.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.math.Vector2D;

/**
 * Class used to perform various GIS computations.
 * 
 * @author pbreugnot
 *
 */
public class GISComputation {
	
	public static double GPS2Meter(double latitudeInDegA, double longitudeInDegA, double latitudeInDegB, double longitudeInDegB){
		//haversine formula https://en.wikipedia.org/wiki/Haversine_formula
		double earthRadius = 6371000;
		double lat1 = Math.toRadians(latitudeInDegA);
		double lon1 = Math.toRadians(longitudeInDegA);
		double lat2 = Math.toRadians(latitudeInDegB);
		double lon2 = Math.toRadians(longitudeInDegB);
		double a = Math.pow(Math.sin((lat1 - lat2)/2),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin((lon1-lon2)/2),2);
		double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = earthRadius*c;
		return d;
	}
	
	public static double[] getVector(double x, double y){
		double[] vector = new double[2];
		return vector;
	}
	
	public static double length2DVector(double x, double y){
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}
	
	public static double[] normalize(double x, double y){
		double[] vector2 = new double[2];
		vector2[0] = x/length2DVector(x, y);
		vector2[1] = y/length2DVector(x, y);
		return vector2;
	}
	
	public static Vector2D directionOfTwoCoordinates(Coordinate a, Coordinate b){
		return new Vector2D(b.x - a.x, b.y - a.y);
	}
	
	/**
	 * Computes the distance in meter between two geographical coordinates,
	 * using the Haversine formula.
	 *
	 * @param a first coordinates
	 * @param b seconf coordinates
	 * @return distance between coordinates in meter
	 */
	public static double GPS2Meter(Coordinate a, Coordinate b){
		return GPS2Meter(a.x, a.y, b.x, b.y);
	}
	
}
